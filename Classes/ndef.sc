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
		var pairs;
		if(this.source.isKindOf(Pbind)) {
			pairs = this.source.patternpairs.asDict.put(selector.asSymbol, args).asPairs;
			this.source = Pbind(*pairs);
		}
	}


	fx { |effects|
		var fxndef = Ndef((this.key++'_fx').asSymbol);
		"Add FX to %: %".format(this.name, effects).postln;
		if( effects.isArray ) {
			effects.do{|effect, i|
				fxndef[i+1] = \filter -> (Ziva.fxDict[effect.asSymbol] ? effect); // second option is a function
			};
			// clear unused indices if needed
			if( effects.size < fxndef.sources.size ) {
				var diff = fxndef.sources.size - effects.size;
				diff.do{|i| fxndef[effects.size + i + 1] = nil };
			};

			fxndef.sources.do{|x| x.postcs};
		} {
			fxndef.sources.size.debug(fxndef.name);
			fxndef[fxndef.sources.size] = \filter -> Ziva.fxDict[effects.asSymbol];
		}
	}
}