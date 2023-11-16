// Live coding in SuperCollider made easy.

// A general class to manage live coding resources.
// Some parts are inspired by or directly taken from:
// - IxiLang by Thor Magnusson
// - Bacalao by Glen Fraser
// - SuperDirt by Julian Rohrhuber

// (C) 2022 Roger Pibernat

// Ziva is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 2 of the License, or (at your
// option) any later version.

// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

Ziva {
	classvar <> server;
	classvar <> fxDict;
	classvar <> tracksDict;
	classvar <> fxBusses;
	classvar <> samplesDict;
	classvar <> rhythmsDict;
	classvar <> clock;
	classvar <> drumDict;
	classvar <> drumMidi;
	classvar <> drumChars;
	classvar <> recsynth; // a synth to record new samples
	classvar <> isRecoring;
	classvar <> constants; // dictionary for constant values
	classvar <> oscillators;
	classvar <> sources; // source functions for Ndef
	classvar <> tracks;

	// *new { |sound|
	// 	^super.new.synth(sound);
	// }

	*initClass {
		this.makeRhythmsDict;
	}

	*start { |inputChannels = 2, outputChannels = 2, server = nil|
		^this.boot(inputChannels, outputChannels, server);
	}

	*boot { |inputChannels = 2, outputChannels = 2, server = nil,
		numBuffers = 16, memSize = 32, maxNodes = 32|
		server = server ? Server.default;
		this.server = server;
		this.serverOptions(this.server, inputChannels, outputChannels, numBuffers, memSize, maxNodes);

		// Create a global variable at the top environment holding Ziva's ProxySpace.
		// Using ~ziva = ProxySpace.new(...) doesn't work.
		// currentEnvironment.put(\ziva, ProxySpace.new(this.server).quant_(1));
		// ~ziva.push;

		// gets called when server boots
		// see: https://doc.sccode.org/Overviews/Methods.html#initTree
		ServerTree.add({this.makeTracks(4)});

		this.server.waitForBoot{
			var allFxGroup;

			ZivaEventTypes.new;
			this.makeConstants;
			this.makeOscillators;
			this.makeSources;
			this.loadSounds;
			this.makeFxDict;
			this.makeDrumDict;
			this.scale_(\major);
			// this.tracks = Array.fill(8, {|i| Ndef((\fxtrack++i).asSymbol, { \in.ar!2 })});

			// // global fx -- last node in the chain
			// // code from https://scsynth.org/t/use-nodeproxy-to-write-effects-on-main-out-channels/2849/2
			// // Create a Group for our NodeProxy after the Server's default
			// // initialize at audio rate
			// Ndef(\all).ar(2);
			// // replace proxy's private bus with hardware bus
			// Ndef(\all).bus = Bus(\audio, 0, 2, server);
			// allFxGroup = Group.after(server.defaultGroup).register;
			// server.sync;
			// Ndef(\all).parentGroup = allFxGroup;

			"r = \\r".interpret;
			this.clock = TempoClock.new(rrand(60,190).debug("tempo")/60).permanent_(true);

		};
		^this.server;
	}

	// get out of Ziva ProxySpace
	*pop {
		topEnvironment.at(\ziva).pop;
	}

	*hush {
		Ndef.dictFor(Ziva.server).keysValuesDo{|k,v|
			if( not( k.asString.beginsWith(\fx_.asString)) && (k != \all) ) {
				k.stop;
			}
		}
	}

	/// \brief	Stop playing all Pdefs
	*stop {
		Pdef.all.collect( _.stop );
	}

	*clear {
		Pdef.removeAll;
		// Server.freeAll;
	}

	*serverOptions { |server = nil, inputChannels = 2, outputChannels = 2, numBuffers = 16, memSize = 32, maxNodes = 32|
		server = server ? Server.default;
		server.options.numBuffers = 1024 * numBuffers; // increase this if you need to load more samples
		server.options.memSize = 8192 * memSize; // increase this if you get "alloc failed" messages
		server.options.maxNodes = 1024 * maxNodes;
		server.options.numInputBusChannels = inputChannels;
		server.options.numOutputBusChannels = outputChannels;
	}

	*scope { |alwaysOnTop = true|
		server.scope(2).style_(2)
		.window.bounds_(Rect( 145 + 485, 0, 200, 250))
		.alwaysOnTop_(alwaysOnTop);
	}

	/// \brief return the Ndef(KEY) exists or NIL if it doesn't exist.
	/// \descritpion This is used to lookup symbols in patterns
	*ndef { | key |
		if( Ndef.dictFor(Ziva.server).activeProxies.indexOf(key).isNil.not ) { ^Ndef(key) };
		^nil
	}

	/// \brief load samples
	*loadSounds {
		"loading sounds".debug;
		this.loadSynths;
		this.loadSamples;
	}

	/// \brief load synthdefs
	*loadSynths { |path|
		// load synthdesclib
		// "loading synths".debug;
		var filePaths;
		path = path ?? { "../synths".resolveRelative };
		filePaths = pathMatch(standardizePath(path +/+ "*"));
		filePaths.do { |filepath|
			if(filepath.splitext.last == "scd") {
				(dirt:this).use { filepath.load };
				"loading synthdefs in %\n".postf(filepath);
			}
		}
	}

	/// \brief Return a list of all compiled SynthDef names
	*synthDefList {
		var names = List.new;

		SynthDescLib.global.synthDescs.keys.asArray.sort.do { |desc|
			// Skip names that start with "system_"
			if("^(system_|pbindFx_)".matchRegexp(desc.asString).not) {
				names.add(desc.asString.asSymbol);
			}
		};

		^names;
	}

	/// \brief	Load samples from directory.
	/// \description
	///  	Load samples from subfolders in the given path.
	///  	Samples will be accessed with folder name and index
	///  	A directory with symlinks should work.
	/// \returns Dictionary
	*loadSamples { arg path, server = nil;
		try {
			path = path ?? {
				PathName(PathName(Ziva.filenameSymbol.asString).parentPath).parentPath+/+"samples"
			};
			this.samplesDict = this.samplesDict ?? { Dictionary.new };
			server = server ? this.server ? Server.default;
			PathName(path).entries.do { |item, i|
				// d.add(item.folderName -> this.loadSamplesArray(item.fullPath, server));
				this.samplesDict.put(item.folderName.asSymbol, this.loadSamplesArray(item.fullPath, server));
			};
			this.listLoadedSamples;
		} {
			// "ERROR: Sample path not set.  Use .loadSamples(PATH).".postln;
			"WARNING: The samples list is empty.  Use .loadSamples(PATH).".postln;
			path.debug("Could not load samples from");
		}
	}

	/// \brief 	load samples from a directory.
	/// \description 	Samples must be files within the given path.
	/// \returns an Array
	*loadSamplesArray { arg path, server = nil;
		var a = Array.new;
		server = server ? this.server ? Server.default;
		PathName(path).entries.do({ |item, i|
			// item.fullPath.postln;
			a = a.add(Buffer.read(server, item.fullPath));
		});
		^a;
	}

	/// \brief	Loads samples from an array of paths into the environment which
	/// 		will make them available as variables with ~ (~folderName).
	/// \returns nothing, variables will be available with 'currentEnvironment[NAME]'
	*loadSamplesAsSymbols { arg paths = [], s = Server.default;
		paths.do { |path|
			var name  = PathName(path).folderName;
			// FIX: Loopier???
			this.samplesDict.put(name.asSymbol, Loopier.loadSamplesArray(path, s));
		};
	}

	*listLoadedSamples {
		this.samplesDict.keys.asArray.sort.do{|k|
			"% (%)".format(k, this.samplesDict[k].size).postln;
		};
		this.samplesDict.size.debug("Total");
	}

	*sample { |name|
		^this.samplesDict.at(name);
	}

	/// TODO: DOCUMENT!!!
	*rec { |name, index, soundin=0, rec=1, feedback=1, length=4, ch=1, monitor=0|
		var recbuf;
		if (name.isNil) {
			// stop recording and exit
			rec = 0;
			"... recording is % -> '%' (% / %)".format(["OFF","ON"][rec], name, index, this.samplesDict[name.asSymbol].size-1).debug;
			^this.recsynth.set(\rec, rec);
		};

		if(this.samplesDict.includesKey(name).not) {
			name.debug("new recording samples group");
			this.samplesDict[name.asSymbol] = List();
		};

		if(index > (this.samplesDict[name.asSymbol].size - 1)){
			"... add new sample to '%'".format(name).postln;
			recbuf = Buffer.alloc(Server.default, Server.default.sampleRate * length, ch);
			this.samplesDict[name.asSymbol].add(recbuf);
			index = this.samplesDict[name.asSymbol].size - 1;
			recbuf = this.samplesDict[name.asSymbol][index];
		};
		"... recording from ch:% is % -> '%' (% / %)".format(soundin, ["OFF","ON"][rec], name, index, this.samplesDict[name.asSymbol].size).debug;
		this.recsynth = this.recsynth ? Synth(\recbuf, [buf: recbuf, rec: 0, feedback: 0, in:soundin, monitor:monitor]);
		this.recsynth.set(\rec, rec, \buf, this.samplesDict[name.asSymbol][index], \in, soundin, \feedback, feedback, \monitor, monitor);
	}

	*stopRec {
		this.rec();
	}

	/// \brief list synth names
	*synths {
		"synths".debug("-----");
		this.synthDefList.collect(_.postln);
		^this.synthDefList;
	}

	/// \brief post synths and samples
	*samples { | path |
		if (path.isNil) {
			"samples".debug("-----");
			this.listLoadedSamples;
		} {
			this.loadSamples(path);
		}
		^this.samplesDict.keys.asArray.sort;
	}

	*sounds {
		this.synths;
		this.samples;
	}

	/// \brief return a random synth name
	*randSynth {
		^Ziva.synthDefList.choose;
	}

	*randSample {
		^Ziva.samplesDict.keys.choose;
	}

	/// \brief return a list of the controls for the given synth
    *synthControls { |synth|
        var controls = List();
        SynthDescLib.global.at(synth).controls.do{ |ctl|
            controls.add([ctl.name, ctl.defaultValue]);
        }
        ^controls
    }

	/// \brief list available methods
	// *help { |class|
	// 	class = class ? Pchain;
	// 	class.methods.collect(_.postln);
	// }

	/// \brief list controls for the given synth
	*controls { |synth|
        "% controls".format(synth).postln;
        this.synthControls(synth).collect(_.postln)
	}

	*makeConstants {
		constants = IdentityDictionary.new;
		constants[\pizz] = 0.1;
		constants[\stass] = 0.25;
		constants[\stacc] = 0.5;
		constants[\tenuto] = 1.0;
		constants[\legato] = 1.1;
		constants[\pedal] = 2;
		constants[\hardleft] = -1;
		constants[\left] = -0.9;
		constants[\right] = 0.9;
		constants[\hardright] = 1;
		constants[\pingpong] = Pseq([-0.9,0.9], inf);
		constants[\rand] = Pwhite(-1,1);
		constants[\ultrafastest] = 1/64;
		constants[\ultrafaster] = 1/32;
		constants[\ultrafast] = 1/16;
		constants[\fastest] = 1/8;
		constants[\faster] = 1/4;
		constants[\fast] = 1/2;
		constants[\slow] = 2;
		constants[\slower] = 4;
		constants[\slowest] = 8;
		constants[\ultraslow] = 16;
		constants[\ultraslower] = 32;
		constants[\ultraslowest] = 64;
		constants[\pppp] = 0.01;
		constants[\ppp] = 0.02;
		constants[\pp] = 0.03;
		constants[\p] = 0.05;
		constants[\f] = 0.2;
		constants[\ff] = 0.3;
		constants[\fff] = 0.5;
		constants[\ffff] = 0.9;
	}

	*makeOscillators {
		oscillators = IdentityDictionary.new;
		oscillators[\sine] = {SinOsc.kr(\freq.kr(1)).range(\min.kr(0),\max.kr(1))};
		oscillators[\saw] = {LFSaw.kr(\freq.kr(1)).range(\min.kr(0),\max.kr(1))};
		oscillators[\pulse] = {LFPulse.kr(\freq.kr(1)).range(\min.kr(0),\max.kr(1))};
		oscillators[\tri] = {LFTri.kr(\freq.kr(1)).range(\min.kr(0),\max.kr(1))};
		oscillators[\noise0] = {LFNoise0.kr(\freq.kr(1)).range(\min.kr(0),\max.kr(1))};
		oscillators[\noise1] = {LFNoise1.kr(\freq.kr(1)).range(\min.kr(0),\max.kr(1))};
		oscillators[\noise2] = {LFNoise2.kr(\freq.kr(1)).range(\min.kr(0),\max.kr(1))};
	}

	*makeSources {
		sources = IdentityDictionary.new;
		sources[\sine] = { SinOsc.ar(\freq.kr(400)) };
		sources[\saw] = { Saw.ar(\freq.kr(400)) };
		sources[\vca] = { \in.ar * \amp.kr(1) };
		sources[\adsr] = { \in.ar * Env.adsr(\atk.kr(0.01), \dec.kr(0.3), \sus.kr(0.5), \rel.kr(1)) };
		sources[\vcf] = { MoogVCF.ar(\in.ar(0), \cutoff.kr(400), \res.kr(0.0)) };
		sources[\pan] = { Pan2.ar(\in.ar, \pan.kr(0)) };
	}

	*makeFxDict { // more to come here + parameter control - for your own effects, simply add a new line to here and it will work out of the box
		fxDict = IdentityDictionary.new;
		fxDict[\reverb] 	= {arg sig; (sig*0.6)+FreeVerb.ar(sig, \mix.kr(0.85), \room.kr(0.86), \damp.kr(0.3))};
		fxDict[\reverbL] 	= {arg sig; (sig*0.6)+FreeVerb.ar(sig, 0.95, 0.96, 0.7)};
		fxDict[\reverbS] 	= {arg sig; (sig*0.6)+FreeVerb.ar(sig, 0.45, 0.46, 0.2)};
		fxDict[\gverbS] 	= {arg sig; sig + GVerb.ar(sig, roomsize:10, revtime:1, damping:0.1, inputbw:0.0, drylevel:0.0, earlyreflevel:1.0, taillevel:0.2, mul: (-6.dbamp))};
		fxDict[\gverb] 		= {arg sig; HPF.ar(GVerb.ar(sig, roomsize:20, revtime:2, damping:0.3, inputbw:0.02, drylevel:0.7, earlyreflevel:0.7, taillevel:0.5), 100)};
		fxDict[\gverbL] 	= {arg sig; HPF.ar(GVerb.ar(sig, roomsize:30, revtime:3, damping:0.3, inputbw:0.5, drylevel:0.5, earlyreflevel:0.5, taillevel:0.5), 100)};
		fxDict[\gverbXL] 	= {arg sig; HPF.ar(GVerb.ar(sig, roomsize:40, revtime:4, damping:0.2, inputbw:0.5, drylevel:0.2, earlyreflevel:0.3, taillevel:0.5), 100)};
		fxDict[\delay]  	= {arg sig; AllpassC.ar(sig, 2, \delt.kr(0.15), \dect.kr(1.3) )};
		fxDict[\lpfS] 		= {arg sig; LPF.ar(sig, \lcutoff.kr(3000))};
		fxDict[\lpf] 		= {arg sig, lcutoff=1000; RLPF.ar(sig, lcutoff, \lres.kr(1.0))};
		fxDict[\lpfL] 		= {arg sig; LPF.ar(sig, \lcutoff.kr(50))};
		fxDict[\hpfS] 		= {arg sig; HPF.ar(sig, \hcutoff.kr(50))};
		fxDict[\hpf]  		= {arg sig; RHPF.ar(sig, \hcutoff.kr(1000), \hres.kr(1.0))};
		fxDict[\hpfL] 		= {arg sig; HPF.ar(sig, \hcutoff.kr(1500))};
		fxDict[\bpf] 		= {arg sig; BPF.ar(sig, \bcutoff.kr(1500), \bres.kr(1.0))};
		fxDict[\tremolo]	= {arg sig; (sig * SinOsc.ar(2.1, 0, 5.44, 0))*0.5};
		fxDict[\vibrato]	= {arg sig; PitchShift.ar(sig, 0.008, SinOsc.ar(2.1, 0, 0.11, 1))};
		fxDict[\techno] 	= {arg sig; RLPF.ar(sig, SinOsc.ar(0.1).exprange(880,12000), 0.2)};
		fxDict[\technosaw] 	= {arg sig; RLPF.ar(sig, LFSaw.ar(0.2).exprange(880,12000), 0.2)};
		fxDict[\distort] 	= {arg sig; (3111.33*sig.distort/(1+(2231.23*sig.abs))).distort*0.02};
		fxDict[\cyberpunk]	= {arg sig; Squiz.ar(sig, 4.5, 5, 0.1)};
		fxDict[\bitcrush]	= {arg sig; Latch.ar(sig, Impulse.ar(11000*0.5)).round(0.5 ** 6.7)};
		fxDict[\antique]	= {arg sig; LPF.ar(sig, 1700) + Dust.ar(7, 0.6)};
		fxDict[\crush]		= {arg sig; sig.round(0.5 ** (\crush.kr(6.6)-1));};
		fxDict[\chorus2]	= {arg sig; Mix.fill(7, {
			var maxdelaytime= rrand(0.005,0.02);
			DelayC.ar(sig, maxdelaytime,LFNoise1.kr(Rand(4.5,10.5),0.25*maxdelaytime,0.75*maxdelaytime) );
		})};
		fxDict[\chorus]	= {arg sig; Mix.fill(7, {
			var maxdelaytime= rrand(0.005,0.02);
			Splay.ar(Array.fill(4,{
				var maxdelaytime= rrand(0.005,0.02);
				var del = DelayC.ar(sig[0], maxdelaytime,LFNoise1.kr(Rand(0.1,0.6),0.25*maxdelaytime,0.75*maxdelaytime));
				// LinXFade2.ar(sig, del, \chorusamt.kr(0.0).linlin(0.0,1.0, -1.0,1.0));
				del;
			}));
		})};
		fxDict[\compress]	= {arg sig; Compander.ar(4*(sig),sig,0.4,1,4,mul:\compressamt.kr(1))};
		fxDict[\limit]	= {arg sig; Compander.ar(4*(sig),sig,0.4,1,4,mul:\compressamt.kr(1))};
		fxDict[\fold] 		= {arg sig; sig.fold(\foldmin.kr(0.01), \foldmax.kr(1))};
	}

	/// \brief	Predefined rhythms to be used with durs
	*makeRhythmsDict {
		var r = \r;
		this.rhythmsDict = IdentityDictionary.new.know_(true);

		// taken from https://www.thejazzpianosite.com/jazz-piano-lessons/jazz-genres/afro-cuban-latin-jazz/
		this.rhythmsDict.put(\clave, 		[[r,r, 1,r, 1,r, r,r], [1,r, r,1, r,r, 1,r]]);
		// this.rhythmsDict.put(\clave32, 		[[1,r, r,1, r,r, 1,r], [r,r, 1,r, 1,r, r,r]]);
		this.rhythmsDict.put(\rumba, 		[[r,r, 1,r, 1,r, r,r], [1,r, r,1, r,r, r,1]]);
		// this.rhythmsDict.put(\rumba32, 		[[1,r, r,1, r,r, r,1], [r,r, 1,r, 1,r, r,r]]);
		this.rhythmsDict.put(\binaneth, 	[[1,r,r,1, 1,r,1,r], [1,r,r,1, 1,r,1,1]]);
		this.rhythmsDict.put(\chitlins, 	[[1,r, r,1, r,r, 1,r], [r,r, 1,r, r,1, r,r]]);
		this.rhythmsDict.put(\cascara, 		[[1,r, 1,r, 1,1, r,1], [1,r, 1,1, r,1, r,1]]);
		this.rhythmsDict.put(\cencerro, 	[[1,r, 1,r, 1,1, 1,1], [r,1, 1,1, 1,r, 1,1]]);
		this.rhythmsDict.put(\cencerru, 	[[1,r, 1,r, 1,r, 1,1], [1,r, 1,1, 1,r, 1,1]]);
		this.rhythmsDict.put(\conga,	 	[[r,r, 1,r, r,r, 1,1], [r,r, 1,1, 1,r, 1,1]]);
		this.rhythmsDict.put(\montuno,	 	[[1,r, 1,1, r,1, r,1], [r,1, r,1, r,1, r,1]]);
		this.rhythmsDict.put(\tumbao,	 	[[1,r, r,1, r,r, 1,r], [1,r, r,1, r,r, 1,r]]);
		this.rhythmsDict.put(\tumbau,	 	[[r,r, r,1, r,r, 1,r], [r,r, r,1, r,r, 1,r]]);
		this.rhythmsDict.put(\horace,	 	[[1,r, r,1, 1,r, r,1], [1,r, r,1, 1,r, r,1]]);
		this.rhythmsDict.put(\buleria,	 	[r,1,r,r,1,r,r,1,r,1,r,1]);
		this.rhythmsDict.put(\claphands,	[1,1,1,r,1,1,r,1,r,1,1,r]);
		this.rhythmsDict.put(\nine,	 		[1,r,r,1,r,1,1,r]);
		this.rhythmsDict.put(\eleven,	 	[1,r,1,r,1,r,1,r,1,r,1]);
		this.rhythmsDict.put(\tonebank,	 	[1,1,1,1, r,1,r,1, 1,1,1,r, 1,r,1,r, 1,1,1,1, r,1,r,1]);
		this.rhythmsDict.put(\tracatrin,	[[1,r,1,r],[1,r,1,r],[1,r,1]]);
		this.rhythmsDict.put(\tracatron,	[[1,r,r],[1,r,r],[1,r,r],[1,r]]);
		this.rhythmsDict.put(\tracatrun,	[[1,r,1,r],[1,r,1,r],[1,r,r]]);
			// this.rhythmsDict.put(\clave23, (durs: [1,1,1,1, 1,0.5,0.5,1,1], rests: [\r,1,1,\r, 1,\r,1,\r,1]));
		// this.rhythmsDict.put(\clave23, (durs: [1,1,2, 1.5,1.5,1], sus: [\r,1,1/2, 2/3,1/3,1]));
	}

	*makeDrumDict {
		// this.drumDict = "brscSlhLftHToyYxXBpiekKOzZ";
		this.drumChars = "brscSlhLftHToyYxXBpiekKOzZ";
		this.drumDict = Dictionary.new;
		this.drumMidi = [
			"Kick Drum",
			"Snare SideStick",
			"Snare Center",
			"Hand Clap",
			"Snare Edge",
			"Floor Tom Center",
			"Closed HiHat",
			"Floor Tom Edge",
			"Pedal HiHat",
			"Tom Center",
			"Semi-Open HiHa",
			"Tom Edge",
			"Swish HiHat",
			"Crash Cymbal 1",
			"Crash Cymbal 1 Choked",
			"Ride Cymbal Tip",
			"Ride Cymbal Choked",
			"Ride Cymbal Bell",
			"Tambourine",
			"Splash Cymbal",
			"Cowbell",
			"Crash Cymbal 2",
			"Crash Cymbal 2 Choked",
			"Ride Cymbal Shank",
			"Crash Cymbal 3",
			"Maracas",
		];

		this.drumMidi.do{ |x,i|
			this.drumDict.put(x, this.drumChars[i]);
		};
	}

	*drums {
		this.drumMidi.do{ |x,i|
			x.debug(this.drumChars[i]);
		};
	}

	*newPlayer { |name, snd|
		var fxname = (name++'_fx').asSymbol;
		name = name.asSymbol;
		if( Ziva.samples.includes(snd.asSymbol) ) {
			Ndef(name, Pbind(\type, \sample, \sound, snd.asSymbol));
		} {
			Ndef(name, Pbind(\instrument, snd.asSymbol));
		};
		Ndef(name).source.postcs;
		Ndef(fxname, { \in.ar(0!2) });
		Ndef(fxname).play;
		Ndef(name) <>> Ndef(fxname);
		name.debug("ndef");
		fxname.debug("ndef_fx");
		^Ndef(name);
	}

	/// \brief	Construct the fx tracks.
	/// \description
	/// 	Make an ndef, and busses to it's input.
	/// 	The bus is stored in the dictionary for outside access.
	*makeTracks { |numtracks|
		this.tracksDict = IdentityDictionary.new;
		// numtracks.do{ |i|
		// 	var ndefsym = (\track_++i).asSymbol;
		// 	var tracksym = (\t++i).asSymbol;
		// 	var bus = Bus.audio(this.server, 2);


		// 	Ndef(ndefsym, { In.ar(bus, 2) }).play;
		// 	this.tracksDict.put(tracksym, bus);
		// 	this.tracksDict[tracksym].debug(ndefsym);
		// };
	}

	*makeTrack { |name|
			var ndefsym = (\track_++name).asSymbol;
			var tracksym = (\t++name).asSymbol;
			var bus = Ndef(ndefsym).bus ? Bus.audio(this.server, 2);

			Ndef(ndefsym, { \in.ar(0!2) * \amp.kr(1) }).play.fadeTime_(1);
			this.tracksDict.put(tracksym, bus);
			this.tracksDict[tracksym].debug(ndefsym);

		ndefsym.debug("new fx track");
	}

	/// \brief set the effects for the track
	/// \param 	track	INT		Track number
	/// \param	effects	ARRAY	Array of effect symbols.
	*track { |track ... effects|
		var sym = (\t++track).asSymbol;
		var ndef = (\track_++track).asSymbol;
		// var exists = Ndef.dictFor(this.server).keys.includes(ndef);//.debug("Ndef exists: %".format(ndef));
		if ( Ndef.dictFor(this.server).keys.includes(ndef).not
			|| this.tracksDict.includesKey(sym).not
			|| Ndef(ndef).source.isNil
		) {
			this.makeTrack(track);
		};
		// clear tracks
		Ndef(ndef).sources.do{|x, i|
			"%: %".format(i, tracksDict[sym]).debug("removing fx for track");
			if(i>0) {
				Ndef(ndef)[i] = nil;
			};
		};
		effects.do{ |effect, i|
			"t%:% -> % : % : %".format(track, i+1, effect, sym, this.tracksDict[sym]).postln;
			Ndef(ndef)[i+1] = \filter -> this.fxDict[effect];
		};
		^Ndef(ndef);
	}

	*fx {
		fxDict.keys.collect(_.postln);
	}

	*rhythms {
		rhythmsDict.keys.asArray.sort.do{|k| var v = Ziva.rhythmsDict[k];  v.asString.replace(" ").replace("r", "·").replace("],[","  ").replace(",").replace("[").replace("]").debug(k)}
	}

	*rhythm { |rh|
		rhythmsDict[rh].postln;
	}

	*rh { this.rhythms }

	*motif { |size=16|
		var pat = (-7..7).pwalk([(-7..-2).prand(1),-1,0,1,(2..7).prand(1)].pwrand([1,4,5,4,1].normalizeSum, inf), startPos:7).asStream;
		^Array.fill(16,{pat.next}).debug("motif");
	}

	/// \brief	Create a Pdef that automatically plays and stores a Ppar
	*pdef { |key, quant=1 ... elements|
		key = key ? \ziva;
		elements = elements.flat;
		elements.debug(key);
		// ^Pdef(key, Ppar(elements)).play(this.clock).quant_(quant);

		// play the harmony only at the beginning
		if (Pdef(\harmony).isPlaying.not.debug("harmony is playing:")) { Pdef(\hamorny).quant_(quant) };

		^Pdef(key, Ppar(elements)).play.quant_(quant);
	}

	*lfo { |index, wave=\sine, freq=1, min=0.0, max=1.0|
		var dict = Dictionary.new;
		dict.put(\sine, {SinOsc.kr(freq).range(min,max)});
		dict.put(\saw, {LFSaw.kr(freq).range(min,max)});
		dict.put(\pulse, {LFPulse.kr(freq).range(min,max)});
		dict.put(\tri, {LFTri.kr(freq).range(min,max)});
		dict.put(\noise0, {LFNoise0.kr(freq).range(min,max)});
		dict.put(\noise1, {LFNoise1.kr(freq).range(min,max)});
		dict.put(\noise2, {LFNoise2.kr(freq).range(min,max)});
		^Ndef((\ziva_lfo++index), dict.at(wave));
	}

	*tempo { ^this.clock.tempo * 60 }
	*tempo_ { |bpm| ^this.clock.tempo = bpm / 60;}
	*bpm { ^this.tempo }
	*bpm_ { |bpm| ^this.tempo( bpm );}

	*scale { ^Pdefn(\scale).source.name }
	*scale_ { | scale | Pdefn(\scale, Scale.at(scale)) }

	// \brief 	create a '~harmony' (and '~h' shortcut) global variable to hold a
	// 			universal harmonic sequence
	// \param 	degs	harmonic progression rootnotes
	// \param 	dur		harmonic rhythm
	// \param 	pairs	any other Pbind parameter
	*harmony { |degs, durs ... pairs|
		degs.debug("hamrmonic degs");
		durs.debug("hamrmonic durs");
		pairs.debug("hamrmonic pairs");
		Pdef(\harmony, Pbind(\amp, 0, \degree, degs, \dur, durs).collect({|event| ~harmony = event })).play;
		^Pfunc { ~harmony[\degree] };
	}
}
