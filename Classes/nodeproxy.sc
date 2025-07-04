+ NodeProxy {
	prSetPbindParam { |param, value|
		if( this.source.class == Pbind ) {
			var pairs = this[0].patternpairs.asDict.put(param.asSymbol, value).asPairs;
			this[0].patternpairs = pairs;
			this[0].patternpairs;
		};
	}

	prGetPbindParam { |param|
		if( this.sourc.class == Pbind ) {
			var pairs = this[0].patternpairs.asDict;
			if( pairs.includesKey(param) ) {
				^pairs.at(param);
			}
		};
	}

	prSymbolToBinaryDigits { |symbol|
		symbol = "0x".catArgs(symbol.asString);
		^symbol.interpret.asBinaryDigits(symbol.replace("0x","").size * 4);
	}

	prCreatePbind { |pairs|
		var key = this.key;
		var basePairs = [\scale, Pdefn(\scale), \root, Pdefn(\root), \animatron, false, \finish, {|e| Ziva.eventToAnimatron(key, e)}];
		this[0] = Pbind(*(basePairs ++ pairs));
		// \fxsend is set to 0 when muting and to 1 when unmuting
		// this allows to mute track without killing the effects (best with delays)
		this[99] = \filterIn -> {|in| in * \fxsend.kr(1)};
		this.reverb(0);
	}

	/// \brief	see `sound`
	s { |snd| this.sound(snd);}

	/// \brief	set the sound
	/// \param	snd:	can be either a synth or a sample
	sound {|snd|
		if( Ziva.samples.includes(snd) ) {
			this.prCreatePbind([\type, \sample, \sound, snd]);
		} {
			this.prCreatePbind([\type, \note, \instrument, snd]);
		};
	}

	mono { |snd|
		this[0] = PmonoArtic(snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
	}

	/// \brief	set a sample index
	/// \param	num:	the index of the samples in the folder (alphabetiaclly ordered)
	n { |num| this.prSetPbindParam(\n, num) }

	/// \brief	connect to MIDI(0)
	/// \param	ch:		channel number [0..15]
	m { |ch| this.midi(ch) }

	midi {|ch|
		// this[0] = Pbind(\type, \midi, \midiout, MIDIOut(0), \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
		this.prCreatePbind([\type, \midi, \midiout, MIDIOut(0), \chan, ch]);
	}

	midiout { |midiout| this.prSetPbindParam(\midiout, MIDIOut(midiout)) }

	finish { |func| this.prSetPbindParam(\finish, func) }
	callback { |func| this.prSetPbindParam(\callback, func) }

	/// \brief	see `zyn`
	z { |ch| this.zyn(ch); }

	/// \brief	send MIDI events and OSC messages to ZynAddSubFx (OSC 127.0.0.1:4001)
	/// \param	ch:		MIDI channel.
	zyn { |ch|
		var key = this.key;
		// this[0] = Pbind(\type, \zynaddsubfx, \midiout, Ziva.zynaddsubfxMIDIOut, \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
		this.prCreatePbind([\type, \zynaddsubfx, \midiout, Ziva.zynaddsubfxMIDIOut, \chan, ch]);
	}

	elektron { |midiout, ch|
		var key = this.key;
		ch = ch - 1; // comply with MIDI standards
		ch.debug("% ch:".format(key));
		// this[0] = Pbind(\type, \elektron, \midiout, midiout, \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
		this.prCreatePbind([\type, \elektron, \midiout, midiout, \chan, ch]);
	}

	analog4 { |ch| this.elektron(Ziva.analog4MIDIOut, ch) }
	digitone { |ch| this.elektron(Ziva.digitoneMIDIOut, ch) }


	/// \brief 	send messages to animatron
	a { |onOrOff = true| this.animatron(onOrOff) }

	animatron { |onOrOff = true|
		this.prSetPbindParam(\animatron, onOrOff);
	}

	// see prCreatePbind docs.
	mute {
		this.set(\fxsend, 0);
	}

	unmute {
		this.set(\fxsend, 1);
	}

	/// \brief	Disable gate on MIDI messages.
	/// \param	value: `true` to disable gate, `false` to enable it
	hold { |value=true|
		// Zynaddsubfx.panic;
		if( value == 1 || value == true ) {
			this.prSetPbindParam(\hasGate, [false].pseq(1));
		} {
			this.prSetPbindParam(\hasGate, true);
		}
	}

	motif{ |size|
		var motif = motif(size);
		this.prSetPbindParam(\dur, motif.dur);
		this.prSetPbindParam(\degree, motif.degree);
	}

	/// \brief	set a rythm with hexadecimal symbol
	/// \descritpion	hexadecimal values will be converted to 8-beat (sic) rythms where 0 is rest and 1 is hit
	/// \param	args:	a symbol representing a hexadecimal number. E.g.: '808a808f'
	r { |args|
		if( args.isString ) { args.asSymbol };
		if( args.isSymbol ) {
			if (args == \r) {args = \0};
			args = this.prSymbolToBinaryDigits(args);
		};
		this.prSetPbindParam(\r, args.replace(0,\r).debug("rhythm").pseq);
	}

	/// \brief Same as `degree` but move by intervals instead of giving absolute notes.
	/// \description
	/// 	Examples:
	/// 	- \u121d31 == (up step leap step) (down leap step)
	/// \param	distances	a symbol describing a sequence of distances.
	/// 					\u set up direction
	/// 					\d sets down direction
	/// 					numbers are steps.
	interval { |distances|
		var dirs = [\u, 1, \d, -1].asDict;
		var dir = 1;
		var deg = 0;
		var degs = List.new;
		distances.asSymbol.asString.do{|c|
			dirs[c.asSymbol].isNil.if {
				deg = deg + (c.asString.asInteger * dir);
				degs.add(deg);
			} {
				dir = dirs[c.asSymbol]
			}
		};
		this.prSetPbindParam(\degree, Pseq(degs,inf));
	}

	fast{ |args| this.prSetPbindParam(\stretch, 1/args) }
	slow{ |args| this.prSetPbindParam(\stretch, args) }

	chance{ |args = 1|
		var chance = args.debug("chance");
		if(args.isNumber) {
			args = max(0, min(args, 1)).debug("chance is number");
			chance = Pwrand([\rest,1], [1-args, args], inf);
		};
		if(args.isArray) {
			"'chance' only accepts number or pattern".warn;
			chance = 1;
		};
		this.prSetPbindParam(\chance, chance);
	}

	/// \brief	stub to send a command to animatron
	///
	cmd { |func| this.prSetPbindParam(\finish, Pfunc(func)) }

	doesNotUnderstand { |selector, args|
		// it's an efect with 'fxN'
		if("^fx\\d+".matchRegexp(selector.asString)) {
			// args.debug(selector);
			this.fx(selector.asString[2..].asInteger, args);
			^this;
		};

		// it's an efect with 'fxN'
		if("^wet\\d+".matchRegexp(selector.asString)) {
			this.drywet(selector.asString[3..].asInteger, args);
			^this;
		};

		// it's an efect with 'fxN'
		if("^mix\\d+".matchRegexp(selector.asString)) {
			this.mix(selector.asString[3..].asInteger, args);
			^this;
		};

		// it's an efect with 'fxN'
		if("^mix\\d+".matchRegexp(selector.asString)) {
			this.mix(selector.asString[3..].asInteger, args);
			^this;
		};

		if(this.source.class == Function) { ^this.set(selector, args) };

		// only convert event and synth keys
		if( Event.parentEvents.default.keys.includes(selector) ||
			Event.parentEvents.synthEvent.keys.includes(selector) ||
			Event.parentEvents.groupEvent.keys.includes(selector) ||
			Event.partialEvents.ampEvent.keys.includes(selector) ||
			Event.partialEvents.bufferEvent.keys.includes(selector) ||
			Event.partialEvents.durEvent.keys.includes(selector) ||
			Event.partialEvents.midiEvent.keys.includes(selector) ||
			Event.partialEvents.nodeEvent.keys.includes(selector) ||
			Event.partialEvents.pitchEvent.keys.includes(selector) ||
			Event.partialEvents.playerEvent.keys.includes(selector) ||
			Event.partialEvents.serverEvent.keys.includes(selector) ||
			Ziva.synthControls(this[0].patternpairs.asDict[\instrument] ? \zivaplaybuf).flat.asDict.keys.includes(selector) ||
			Zynaddsubfx.oscInterfaceDict.includesKey(selector)
			// selector == \r || selector == \rh || selector == \rhythm
		) {
			if( args.isSymbol ) {
				this.prSetPbindParam(selector, Pkey(args));
			}{
				this.prSetPbindParam(selector, args);
			}
		};
	}

	lag { |seconds| this.prSetPbindParam(\lag, seconds) }

	///	\brief	set an effect with an index
	/// \description	effects can be added in any order
	/// \param	index:	index of the effect (indices < 100 will be converted to 100 + index)
	/// \param	effect:	either a symbol with an effect name, or a function using the signal input {|in| ...}
	fx { |index, effect|
		if(index < 100){
			index = index.asInteger + 100;
			"[WARNING] Index for % < 100 has been reindexed: % \n".postf(this.key, index);
		};

		if(this[0].isNil) {
			// this[0] = { \in.ar(0!2) };
			this[0] = Pbind(\amp, 0);
		};

		if( effect.isNil ) {
			this[index] = nil;
		} {
			this[index] = \filterIn -> (Ziva.fxDict[effect.asSymbol] ? effect);
			this.drywet(0.5);
		};
	}

	///	\brief	set the dry (0) - wet (1) value of the effect at that index
	///	\param	index:	index of the effect
	///	\param	amt:	0 = 100% dry signal; 1 = 100% wet signal
	drywet { |index, amt|
		if(index < 100){
			index = index.asInteger + 100;
		};
		(\wet++index).asSymbol.debug("drywet");
		this.set((\wet++index).asSymbol, amt)
	}

	gain { |gain = 1|
		var index = ("track\\d+").matchRegexp(this.key.asString).if { this.key.asString.findRegexp("\\d+")[0][1].asInteger };
		var mixer = Ziva.proxyspace.at(\mixer);
		mixer.set((\mix++index).asSymbol, gain);
	}

	/// \brief	set the amount of reverb.
	reverb { |wet = 0.0|
		var index = ("track\\d+").matchRegexp(this.key.asString).if { this.key.asString.findRegexp("\\d+")[0][1].asInteger };
		var amp = this.prGetPbindParam(\amp) * (1 - wet);
		var dry = amp * (1 - wet);
		Ziva.proxyspace[\reverb][index] = \mix -> Ziva.proxyspace[this.key];
		Ziva.proxyspace[\reverb].set((\mix++index).asSymbol, wet);
		this.amp = dry;
	}

	fadein { |dur|
		var fadekey = this.key++\fadein;
		Ziva.proxyspace[fadekey] = {Line(0,1,dur,doneAction:2)};
		this.prSetPbindParam(\gain, Ziva.proxyspace[fadekey]);
	}

	fadeout { |dur|
		var fadekey = this.key++\fadeout;
		var currentgain = this.prGetPbindParam(\gain);
		Ziva.proxyspace[fadekey] = {Line(currentgain,0,dur,doneAction:2)};
		this.gain(Ziva.proxyspace[fadekey]);
	}

	/// \brief	send output to destination
	/// \descriptions	send the signal on the left to the Nth slot of the signal on the right.
	/// 	This can be used to send signals through a common fx chain before sending it to the mixer.
	/// 	Usage:
	///
	/// 	~track1 fx1: \reverb			// create a track with a reverb (note we don't specify a sound; no sound `s:`)
	/// 	~sndA =>>.1 ~track1 mix1: 0.5 	// set ~sndA to channel 1 of this track
	/// 	~sndB =>>.2 ~track1 mix2: 0.2 	// set ~sndB to channel 2 of this track
	/// 	~track1 >>>.1 0.2 			Adverbs for Binar	// send the track output to the mixer
	///
	/// \param	destination:	another NodeProxy (instrument or track)
	/// \param	index:			(adverb) usage: =>>.N - index or "channel" of the destination where the signal is sent
	=>> { |destination, index=\1|
		destination.unpatch(index);
		destination.addSource(index.asInteger, this);
		destination.set((\mix++index).asSymbol, 0.9);
		^destination;
	}

	/// \brief	unpatch a signal from a destination
	/// \param	destination:	another NodeProxy (instrument or track)
	/// \param	index:			(adverb) usage: =>>.N - index or "channel" of the destination where the signal is sent
	=<< { |destination, index=\1|
		destination.unpatch(index);
	}

	/// \brief	see =<<
	unpatch { |index=\1|
		this.set((\mix++index), nil);
		this.[index.asInteger] = nil;
	}

	/// \brief	send a signal to a destination
	to { |destination, index=\1, mixAmt=1|
		destination.addSource(index.asInteger, this);
		destination.mix(index.asInteger, mixAmt);
	}

	/// \brief	send a signal to the mixer
	/// \description	There's a main mixer where all signals must be patched in order to hear the sound.
	/// 	The mixer channel is specified with a number.
	/// 	usage:
	///
	/// 	~sndA >>>.2 0.5	// patch ~sndA's signal output to main mixer's channel 2 with a gain of 0.5
	///	\param	mixAmt:	gain
	///	\param	index:	(adverb) usage: >>>.N - index or channel in the mixer where the signal will be patched
	>>> { |mixAmt=0.1, index=\1|
		this.mixer(index.asInteger, mixAmt);
	}

	<<< {
		this.mixer.sources;
	}

	mixer { |index=1, mixAmt=0.1|
		var mixer = Ziva.proxyspace.at(\mixer);
		mixer.addSource(index.asInteger, this);
		mixer.set((\mix++index).asSymbol, mixAmt);
	}

	addSource { |index, source|
		if(this[0].isNil) {
			this[0] = { \in.ar(0!2) };
		};

		if( source.isNil ) {
			this[index] = nil;
		} {
			this[index] = \mix -> source;
		};
	}

	mix { |index, gain=0.1|
		if(this[0].isNil) {
			this[0] = { \in.ar(0!2) };
		};

		if( gain.isNil.not ) {
			this.set((\mix++index).asSymbol, gain);
		};
	}

	/// \brief	set an LFO with a function
	/// \description	LFOs can be used to modulate parameters.
	/// \usage
	///		~freq lfo: sine(1, 400, 3000);
	///		~res lfo: sine(1, 0, 0.7);
	///		~sndA s: \saw octave: 3 fx1: vcf(~freq, ~res) >>>.1 1;
	/// \param	func:	function that defines the modulation - see `Functions`
	lfo { |func|
		this[0] = func;
	}

	line	{ | end=1.0, dur=10 | ^{Line.ar(this, end, dur)}}
	sine	{ | min=(-1), max=1, amp=1, phase=0 | ^{SinOsc.ar(this, phase).range(min,max) * amp}}
	tri		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFTri.ar(this, phase).range(min,max) * amp}}
	saw		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFSaw.ar(this, phase).range(min,max) * amp}}
	pulse	{ | min=(-1), max=1, amp=1, width=0.5, phase=0 | ^{LFPulse.ar(this, phase, width).range(min,max) * amp}}
	noise0	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise0.ar(this).range(min,max) * amp}}
	noise1	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise1.ar(this).range(min,max) * amp}}
	noise2	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise2.ar(this).range(min,max) * amp}}

	/// \brief	set a seed to get persistent randomness
	/// \description	keep the same random values when reevaluating
	/// \usage			~snd.seed(1234) s: \bass degree: [0,2,4].pick(8) >>>.1 1
	/// \param	num:	same results will come back when restoring a number
    seed { |num| thisThread.randSeed = num }

	unison{ |detune=0.1| this.prSetPbindParam(\detune, detune);}

	freereverb {| room=0.86, damp=0.3 | ^{| in | (in*0.6) + FreeVerb.ar(in, this, room, damp)} }
	gverb {| time, damp | ^{| in | HPF.ar(GVerb.ar(in, roomsize:this, revtime:time, damping:damp, inputbw:0.02, drylevel:0.7, earlyreflevel:0.7, taillevel:0.5), 100)}}
	fbdelay {| fb=0.8 |
		^{| in |
			var local;
			// read feedback , add to source
			local = LocalIn.ar(2) + in;
			// delay sound
			local = DelayN.ar(local, 4, this);
			// reverse channels to give ping pong effect, apply decay factor
			// LocalOut.ar(local.reverse * fb);
			LocalOut.ar(local * fb);
			local
		}
	}
	lpf {| res = 1 | ^{| in | RLPF.ar(in, this, res)}}
	hpf {| res = 1 | ^{| in | RHPF.ar(in, this, res)}}
	bpf {| res = 1 | ^{| in | BPF.ar(in, this, res)}}
	brf {| res = 1 | ^{| in | BRF.ar(in, this, res)}}
	vcf {| res=0.7, mul=1 |  ^{| in | MoogVCF.ar(in, this, res, mul: mul)} }
	tremolo {| depth=0.3 | ^{| in | in * SinOsc.ar(this, 0, depth, 0)}}
	vibrato {| depth=0.3 | ^{| in | PitchShift.ar(in, 0.008, SinOsc.ar(this, 0, depth, 1))}}
	crush {^{| in | in.round(0.5 ** (this-1));}}
	compress {^{| in | Compander.ar(4*(in),in,0.4,1,4,mul:this)}}
	limit {| dur=0.01 | ^{| in | Limiter(in, this, dur)}}
	distor { |smooth=0.5, post=1| ^{|in| CrossoverDistortion.ar(in, this, smooth) * post }}
	// asymetric fold
	// \param neg	absolute value of the negative pole value, will be converted to negative
	// -- old version -- fold {| max=1 | ^{| in | LeakDC.ar( in.fold(this, max) )}}
	afold { |neg, post=1|
		var posPre = this.max(0.01);
		var negPre = neg.max(0.01).neg;
		^{| in |
			LeakDC.ar(in.fold(negPre, posPre) * (1/negPre.abs + posPre)) * post
		}
	}
	// // symetric fold
	wavefold { |post=1| ^{| in | in.fold2(this.max(0.01)) * (1/this.max(0.01))  * post }}

	left { |amt=1| this.pan(amt.neg) }
	right { |amt=1| this.pan(amt) }
}