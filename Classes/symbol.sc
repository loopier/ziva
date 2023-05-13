+ Symbol {
	prSynthOrSample { | ... pairs |
		var pat;
		if (SynthDescLib.global.synthDescs.keys.asArray.sort.indexOf(this).isNil.not) {
			// Pdefn(\scale) is defined in Ziva.sc
			pat = Psynth(this, \scale, Pdefn(\scale), *pairs);
		} {
			pat = Psample(this, *pairs);
		};

		^pat;
	}

	ndef	{ | ... args | ^Ndef(this, *args) }
	sine	{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {SinOsc.ar(freq, phase).range(min,max) * amp})}
	tri		{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFTri.ar(freq, phase).range(min,max) * amp})}
	saw		{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFSaw.ar(freq, phase).range(min,max) * amp})}
	pulse	{ | freq, min=(-1), max=1, amp=1, width=0.5, phase=0 | ^Ndef(this, {LFPulse.ar(freq, phase, width).range(min,max) * amp})}
	noise0	{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFNoise0.ar(freq).range(min,max) * amp})}
	noise1	{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFNoise1.ar(freq).range(min,max) * amp})}
	noise2	{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFNoise2.ar(freq).range(min,max) * amp})}


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
		if(Ndef(this).isPlaying.not) {
			Ndef(this).quant = 1;
			Ndef(this).fadeTime = 0.5;
			Ndef(this).clock = Ziva.clock;
			Ndef(this).play;
		} {
			Ndef(this).resume
		};

		if(args.size > 0) {Ndef(this, Ppar(args))};

		^Ndef(this);
	}

	stop	{ ^Ndef(this).pause }
	pause	{ ^Ndef(this).pause }
	resume	{ ^Ndef(this).resume }
	mute	{ ^Ndef(this).pause }
	unmute	{ ^Ndef(this).resume }

	solo	{
		Ndef.dictFor(Ziva.server).keysValuesDo{|k,v|
			if( not( k.asString.beginsWith(\fx_.asString)) ) {
				if(k != this) {
					k.debug("mute").mute
				} {
					k.debug("unmute").unmute
				}
			}
		}
		^Ndef(this);
	}

	unsolo {
		Ndef.dictFor(Ziva.server).keysValuesDo{|k,v|
			if( not( k.asString.beginsWith(\fx_.asString)) ) {
				k.unmute;
			}
		}
		^Ndef(this);
	}

	lfo { | args | ^Ndef(this, Ziva.oscillators[args[0]]).set(*args[1..]) }

	deg { | ... args |
		^this.prSynthOrSample(*args);
	}

	rh { | args | ^this.prSynthOrSample.rh(args) }

	fx { | effects |
		var fxNdef = Ndef(('fx_'++this).asSymbol);
		if(fxNdef.source.isNil) {
			// var bus = fxNdef.bus ? Bus.audio(Ziva.server, 2);
			fxNdef.source = {|in| Ndef(this).ar * \amp.kr(1) };
			fxNdef.quant = 1;
			fxNdef.fadeTime = 0.5;
			fxNdef.clock = Ziva.clock;
			fxNdef.play;
			// fxNdef <<> Ndef(this);
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

	set { | ... pairs |
		Ndef(('\fx_'++this).asSymbol).set(*pairs);
	}

	drywet { | amt = 0.5 |
		// use Ndefs volume ??
		Ndef(this).vol = 1 - amt;
		Ndef(('fx_'++this).asSymbol).vol = amt;
	}

	scramble { ^this.asString.scramble.asSymbol }

}