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

	lfo { |func|
		this.source = func;
	}

	// functions
	lpf {| res = 1 | ^{| in | RLPF.ar(in, this, res)}}
	hpf {| res = 1 | ^{| in | RHPF.ar(in, this, res)}}
	bpf {| res = 1 | ^{| in | BPF.ar(in, this, res)}}

	// wet { | index, amt=1 | Ndef((this.key++'_fx')).set((\wet++index).asSymbol, amt) }
	wet { | index, amt=1 | this.set((\wet++index).asSymbol, amt) }

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
}