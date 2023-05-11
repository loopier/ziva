+ Symbol {
	prSynthOrSample { | ... pairs |
		var pat;
		if (SynthDescLib.global.synthDescs.keys.asArray.sort.indexOf(this).isNil.not) {
			pat = Psynth(this, *pairs);
		} {
			pat = Psample(this, *pairs);
		};

		^pat;
	}

	synth { | ... args |
		^this.play([Psynth(*args)]);
	}

	sample { | ... args |
		^this.play([Psample(*args)]);
	}

	midi { | ... args |
		^this.play([Pmidi(*args)]);
	}


	play { | args |
		args.debug("play (%)".format(args.class));
		// args = args ? [nil];
		// args.debug("play");
		// if( args.isArray && args.size == 0 ) { args.add(nil) };
		// if( args.isSymbol ) { args = [Psynth(args)] };
		// args.debug("play");
		^Ndef(this, Ppar(args)).quant_(1).fadeTime_(0.5).play;
	}

	stop { | args | ^Ndef(this).stop }
	lfo { | args | ^Ndef(this, Ziva.oscillators[args[0]]).set(*args[1..]) }

	deg { | ... args |
		^this.prSynthOrSample(*args);
	}

	rh { | args | ^this.prSynthOrSample.rh(args) }

	fx { | effects |
		var fxNdef = Ndef(('fx_'++this).asSymbol);
		if(fxNdef.source.isNil) {
			// var bus = Bus.audio(Ziva.server, 2);
			fxNdef.source = {|in| Ndef(this).ar * \amp.kr(1) };
			fxNdef.quant = 1;
			fxNdef.fadeTime = 0.5;
			fxNdef.play;
		};

		effects.do{ |effect, i|
			if( effect.isSymbol ) { effect = (func: Ziva.fxDict[effect], args: []) };
			if( effect.isFunction ) { effect = (func: effect, args: []) };
			{ effect = effect.().debug("effect") };
			fxNdef[i+1] = \filter -> effect[\func];
			fxNdef.set(*effect[\args]);
		};

		// clear unused fx
		fxNdef.sources.do{|x,i| if(i > effects.size) {fxNdef.sources = nil}};

		^fxNdef;
	}

	drywet { | amt = 0.5 |
		// use Ndefs volume ??
		Ndef(this).vol = 1 - amt;
		Ndef(('fx_'++this).asSymbol).vol = amt;
	}

	scramble { ^this.asString.scramble.asSymbol }

}