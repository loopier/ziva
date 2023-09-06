+ NodeProxy {

	synth { | synth |
		this.play;
		this.quant = 1;
		this.source = Psynth(synth);
	}

	sample { | sample |
		this.play;
		this.quant = 1;
		this.source = Psample(sample);
	}

	midi { | channel |
		this.play;
		this.quant = 1;
		this.source = Pmidi(MIDIOut(0), \chan, channel);
	}

	lfo { | func |
		if( func.isSymbol ) {
			this.source = Ziva.oscillators[func];
		}{
			this.source = func;
		}
	}

	prSendParamToPbind { |param, value|
		var pairs;
		if(this.source.isNil) {^nil};
		pairs = this.source.patternpairs;
		value.debug("values");
		pairs = pairs.asDict;
		pairs[param] = value;
		pairs = pairs.asPairs;
		pairs.debug("pairs");
		^Pbind(*pairs);
	}

	oct { |value| this.source = this.prSendParamToPbind(\octave, value) }
	deg { |value| this.source = this.prSendParamToPbind(\degree, value) }
	leg { |value| this.source = this.prSendParamToPbind(\legato, value) }
	// atk { |value| this.source = this.prSendParamToPbind(\attack, value) }
	// dec { |value| this.source = this.prSendParamToPbind(\decay, value) }
	// sus { |value| this.source = this.prSendParamToPbind(\sustain, value) }
	// rel { |value| this.source = this.prSendParamToPbind(\release, value) }

	doesNotUnderstand { |selector ...args|
		// selector.debug((this.class ++ " does not understand method").asString);
        // super.findRespondingMethodFor(selector);
		// FIX: call super.doesNotUnderstand
		selector.debug("Sending to pbind");
		this.source = this.prSendParamToPbind(selector, *args);
		// this.set(selector, *args);
		// ^this;
	}

	fx { | effect |
		if( effect.isSymbol ) {
			this.add(\filter -> Ziva.fxDict[effect.asSymbol]);
		} {
			this.add(\filter -> effect);
		}
	}

	// fold {
	// 	^{|in| Fold.ar(in, this, this.neg) }
	// }
}