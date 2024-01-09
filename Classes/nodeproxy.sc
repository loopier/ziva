+ NodeProxy {
	prSetPbindParam { |param, value|
		var pairs = this.source.patternpairs.asDict.put(param.asSymbol, value).asPairs;
		this.source.patternpairs = pairs;
		this.source.patternpairs.debug(value.class);
	}

	sound {|snd|
		if( Ziva.samples.includes(snd) ) {
			this.source = Pbind(\type, \sample, \sound, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
		} {
			this.source = Pbind(\type, \note, \instrument, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
		};
	}

	s { |snd|
		this.sound(snd);
	}

	midi {|ch|
		this.source = Pbind(\type, \midi, \midiout, MIDIOut(0), \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
	}

	// zynaddsubfx
	zyn { |ch|
		this.source = Pbind(\type, \zynaddsubfx, \midiout, MIDIOut(0), \chan, ch, \scale, Pdefn(\scale), \root, Pdefn(\root));
	}

	z { |ch| this.zyn(ch); }

	n { |num| this.prSetPbindParam(\n, num) }

	doesNotUnderstand { |selector, args|
		// it's an efect with 'fxN'
		if("^fx\\d+".matchRegexp(selector.asString)) {
			args.debug(selector);
			this.fx(selector.asString[2..].asInteger, args);
			^this;
		};

		// it's an efect with 'fxN'
		if("^wet\\d+".matchRegexp(selector.asString)) {
			this.drywet(selector.asString[3..].asInteger, args);
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
			Zynaddsubfx.respondsTo(selector)
		) {
			this.prSetPbindParam(selector, args);
		};
	}

	fx { |index, effect|
		if(this.source.isNil) {
			this.source = { \in.ar(0!2) };
		};

		if( effect.isNil ) {
			this[index] = nil;
		} {
			this[index] = \filter -> (Ziva.fxDict[effect.asSymbol] ? effect);
		};
	}

	drywet { |index, amt|
		(\wet++index).asSymbol.debug("drywet");
		this.set((\wet++index).asSymbol, amt)
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
	fold {| max=1 | ^{| in | LeakDC.ar( in.fold(this, max) )}}
}