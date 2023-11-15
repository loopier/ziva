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
		var fxndef = Ndef((this.key++'_fx').asSymbol);
		if( effects.isArray ) {
			effects.do{|effect, i|
				fxndef[i+1] = \filter -> Ziva.fxDict[effect.asSymbol];
			};
			// clear rest
			fxndef.sources[effects.size..] = nil;
		} {
			fxndef.sources.size.debug(fxndef.name);
			fxndef[fxndef.sources.size] = \filter -> Ziva.fxDict[effects.asSymbol];
		}
	}
}