+ NodeProxy {

	synth { | synth |
		if(Ndef(synth).source.isNil) {
			synth.debug("Creating new synth");
			this.source = Psynth(synth);
			this.quant = 1;
			this.play;
		} {
			this.source = this.prSendParamToPbind(\instrument, \synth)
		}
		// ^this;
	}

	sample { | sample |
		this.source = Psample(sample);
		this.quant = 1;
		this.play;
		^this;
	}

	prSendParamToPbind { |param, value|
		var pairs = this.source.patternpairs;
		pairs = pairs.asDict;
		pairs[param] = value;
		pairs = pairs.asPairs;
		param.debug("Sending pattern parameter");
		pairs.debug("pairs");
		^Pbind(*pairs);
	}

	doesNotUnderstand { |selector ...args|
		// selector.debug((this.class ++ " does not understand method").asString);
        // super.findRespondingMethodFor(selector);
		// FIX: call super.doesNotUnderstand
		selector.debug("Sending to pbind");
		this.source = this.prSendParamToPbind(selector, *args);
		// this.set(selector, *args);
		// ^this;
	}

	// dur { | ...args |
	// 	this.prSendParamToPbind(\dur, *args);
	// 	// ^this;
	// }

	fx { | effect |
		this.add(\filter -> Ziva.fxDict[effect.asSymbol]);
	}
}