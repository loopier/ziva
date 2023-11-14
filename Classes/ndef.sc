+ Ndef {
	sound {|snd|
		if( Ziva.samples.includes(snd) ) {
			this.source = Pbind(\type, \sample, \sound, snd);
		} {
			this.source = Pbind(\type, \note, \instrument, snd);
		}
	}

	s { |snd| this.sound(snd) }

	doesNotUnderstand { |selector, args|
		var pairs = this.source.patternpairs.asDict.put(selector.asSymbol, args).asPairs;
		this.source = Pbind(*pairs);
	}


	fx { |effects|
		var fxname = this.key.debug("fx to key");
		// ndef[ndef.sources.size] = \filter -> Ziva.fxDict[effect.asSymbol];
	}
}