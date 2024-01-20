+ NodeProxy {
	prSetPbindParam { |param, value|
		var pairs = this.source.patternpairs.asDict.put(param.asSymbol, value).asPairs;
		this.source.patternpairs = pairs;
		this.source.patternpairs;
	}

	prSymbolToBinaryDigits { |symbol|
		symbol = "0x".catArgs(symbol.asString);
		^symbol.interpret.asBinaryDigits(symbol.replace("0x","").size * 4);
	}

	s { |snd| this.sound(snd);}

	sound {|snd|
		if( Ziva.samples.includes(snd) ) {
			this.source = Pbind(\type, \sample, \sound, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
		} {
			this.source = Pbind(\type, \note, \instrument, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
		};
	}

	// samples
	n { |num| this.prSetPbindParam(\n, num) }

	// midi
	m { |ch| this.midi(ch) }

	midi {|ch|
		this.source = Pbind(\type, \midi, \midiout, MIDIOut(0), \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
	}

	// zynaddsubfx
	z { |ch| this.zyn(ch); }

	zyn { |ch|
		this.source = Pbind(\type, \zynaddsubfx, \midiout, MIDIOut(0), \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
	}

	hold { |value=true|
		// Zynaddsubfx.panic;
		if( value == 1 || value == true ) {
			this.prSetPbindParam(\hasGate, [false].pseq(1));
		} {
			this.prSetPbindParam(\hasGate, true);
		}
	}

	// rhythms
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
			Ziva.synthControls(this.source.patternpairs.asDict[\instrument] ? \zivaplaybuf).flat.asDict.keys.includes(selector) ||
			Zynaddsubfx.oscInterfaceDict.includesKey(selector)
		) {
			this.prSetPbindParam(selector, args);
		};
	}

	fx { |index, effect|
		if(index < 100){
			index = index.asInteger + 100;
			"[WARNING] Index for % < 100 has been reindexed: % \n".postf(this.key, index);
		};

		if(this.source.isNil) {
			// this.source = { \in.ar(0!2) };
			this.source = Pbind(\amp, 0);
		};

		if( effect.isNil ) {
			this[index] = nil;
		} {
			this[index] = \filterIn -> (Ziva.fxDict[effect.asSymbol] ? effect);
		};
	}

	drywet { |index, amt|
		if(index < 100){
			index = index.asInteger + 100;
		};
		(\wet++index).asSymbol.debug("drywet");
		this.set((\wet++index).asSymbol, amt)
	}

	// send output to destination
	// second argument is adverb
	// usage:
	//
	// ~sound =>.2 ~mixer
	=> { |destination, index=\1|
		destination.unpatch(index);
		destination.addSource(index.asInteger, this);
		destination.set((\mix++index).asSymbol, 0.9);
		^destination;
	}

	// unmap patch
	=< { |destination, index=\1|
		destination.unpatch(index);
	}

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
	// 	if( this.source.isNil ) { this.source = Pbind(\amp, 0) };
	// 	this.addSource(index.asInteger, source);
	// }

	to { |destination, index=\1, mixAmt=1|
		destination.addSource(index.asInteger, this);
		destination.mix(index.asInteger, mixAmt);
	}

	// second argument is adverb
	// usage:
	//
	// ~sound >>>.2 0.5
	//
	// automatically patch ~sound to a mixer channel with a value
	// -- shortcut for ~mixer <=.N ~mixer mixN: amount
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
		if(this.source.isNil) {
			this.source = { \in.ar(0!2) };
		};

		if( source.isNil ) {
			this[index] = nil;
		} {
			this[index] = \mix -> source;
		};
	}

	mix { |index, gain=0.1|
		if(this.source.isNil) {
			this.source = { \in.ar(0!2) };
		};

		if( gain.isNil.not ) {
			this.set((\mix++index).asSymbol, gain);
		};
	}

	lfo { |func|
		this.source = func;
	}

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