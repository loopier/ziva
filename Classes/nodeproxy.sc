+ NodeProxy {
	sound {|snd|
		if( Ziva.samples.includes(snd) ) {
			this.source = Pbind(\type, \sample, \sound, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
		} {
			this.source = Pbind(\type, \note, \instrument, snd, \scale, Pdefn(\scale), \root, Pdefn(\root));
		};
	}

	s { |snd| this.sound(snd) }

	doesNotUnderstand { |selector, args|
		// it's an efect with 'fxN'
		if("^fx\\d+".matchRegexp(selector.asString)) {
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
			Ziva.synthControls(this.source.patternpairs.asDict[\instrument]).flat.asDict.keys.includes(selector)
		) {
			var pairs = this.source.patternpairs.asDict.put(selector.asSymbol, args).asPairs;
			this.source.patternpairs = pairs;
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
}