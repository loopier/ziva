+ NodeProxy {
	prSetPbindParam { |param, value|
		var pairs = this[0].patternpairs.asDict.put(param.asSymbol, value).asPairs;
		this[0].patternpairs = pairs;
		this[0].patternpairs;
	}

	prSymbolToBinaryDigits { |symbol|
		symbol = "0x".catArgs(symbol.asString);
		^symbol.interpret.asBinaryDigits(symbol.replace("0x","").size * 4);
	}

	/// \brief	see `sound`
	s { |snd| this.sound(snd);}

	/// \brief	set the sound
	/// \param	snd:	can be either a synth or a sample
	sound {|snd|
		if( Ziva.samples.includes(snd) ) {
			this[0] = Pbind(\type, \sample, \sound, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
		} {
			this[0] = Pbind(\type, \note, \instrument, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
		};
	}

	/// \brief	set a sample index
	/// \param	num:	the index of the samples in the folder (alphabetiaclly ordered)
	n { |num| this.prSetPbindParam(\n, num) }

	/// \brief	connect to MIDI(0)
	/// \param	ch:		channel number [0..15]
	m { |ch| this.midi(ch) }

	midi {|ch|
		this[0] = Pbind(\type, \midi, \midiout, MIDIOut(0), \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
	}

	/// \brief	see `zyn`
	z { |ch| this.zyn(ch); }

	/// \brief	send MIDI events and OSC messages to ZynAddSubFx (OSC 127.0.0.1:4001)
	/// \param	ch:		MIDI channel.
	zyn { |ch|
		this[0] = Pbind(\type, \zynaddsubfx, \midiout, MIDIOut(0), \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
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

	/// \brief	set a rythm with hexadecimal symbol
	/// \descritpion	hexadecimal values will be converted to 8-beat (sic) rythms where 0 is rest and 1 is hit
	/// \param	args:	a symbol representing a hexadecimal number. E.g.: '808a808f'
	r { |args|
		if( args.isSymbol ) {
			args = this.prSymbolToBinaryDigits(args);
		};
		this.prSetPbindParam(\r, args.replace(0,\r).debug("rhythm").pseq);
	}

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

		// only convert event and synth keys
		if(
			Event.parentEvents.default.keys.includes(selector) ||
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
		) {
			this.prSetPbindParam(selector, args);
		};
	}

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

	// // second argument is adverb
	// // usage:
	// //
	// // ~mixer <=.2 ~sound
	// //
	// // this will set Ndef(\sound).source to slot Ndef(\mixer)[2]
	// <= { |source, index=\1|
	// 	if( this[0].isNil ) { this[0] = Pbind(\amp, 0) };
	// 	this.addSource(index.asInteger, source);
	// }

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

	/// \brief	set a seed to get persistent randomness
	/// \description	keep the same random values when reevaluating
	/// \usage			~snd.seed(1234) s: \bass degree: [0,2,4].pick(8) >>>.1 1
	/// \param	num:	same results will come back when restoring a number
    seed { |num| thisThread.randSeed = num }

	freereverb {| room=0.86, damp=0.3 | ^{| in | (in*0.6) + FreeVerb.ar(in, this, room, damp)} }
	reverb {| room=0.86, damp=0.3 | ^this.freeverb(room, damp) }
	gverb {| time, damp | ^{| in | HPF.ar(GVerb.ar(in, roomsize:this, revtime:time, damping:damp, inputbw:0.02, drylevel:0.7, earlyreflevel:0.7, taillevel:0.5), 100)}}
	delay {| decay=0 | ^{| in | AllpassC.ar(in, 4, this, decay )}}
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
	// symetric fold
	fold { |post=1| ^{| in | in.fold2(this.max(0.01)) * (1/this.max(0.01))  * post }}
}