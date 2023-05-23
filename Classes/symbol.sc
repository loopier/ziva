+ Symbol {
	prSynthOrSample { | ... pairs |
		^if (SynthDescLib.global.synthDescs.keys.includes(this)) {
			// Pdefn(\scale) is defined in Ziva.sc
			Psynth(this, \scale, Pdefn(\scale), *pairs);
		} {
			Psample(this, *pairs);
		};
	}

	prGetFxNdef {
		^if( this == \all ) {
			Ndef(this)
		} {
			Ndef(('fx_'++this).asSymbol)
		}
	}

	asRhythm { ^this.asString.asBinaryDigits.flat.replace(0,\r) }

	asDegrees { ^this.asScaleDegrees }
	asScaleDegrees {
		var value = this.asString;
		^Array.fill(value.size, {|i| value[i].asString.asHexIfPossible});
	}

	// return list of durations where each hex value is 1/hex (expcept 0 = 2)
	asDurs {
		var args = this.asString;
		args = Array.fill(args.size, {|i|
			var val;
			if( args[i].asString.asHexIfPossible == 0 ) { val = 2 } { val = 1 / args[i].asString.asHexIfPossible };
		});
		^args;
	}

	// Interop with Bacalao pattern parsing (https://github.com/totalgee/bacalao/)
	<> { arg pattern, adverb;
		var validateMethod = { arg receiver, method, typeMsg;
			if (receiver.respondsTo(method).not) {
				Error("Bacalao needs to be installed to use '<>' with % patterns\n  Use Quarks.install(\"https://github.com/totalgee/bacalao/\")".format(typeMsg)).throw;
			}
		};
		pattern = case
		{ pattern.isKindOf(String) } {
			validateMethod.(pattern, 'bparse', "string");
			pattern.bparse(adverb ? \degree, inf);
		}
		{ pattern.isKindOf(Symbol) } {
			pattern = pattern.asString;
			validateMethod.(pattern, 'cparse', "character");
			pattern.cparse(adverb ? \degree, inf);
		}
		{ pattern };
		^Pchain(this.prSynthOrSample, pattern);
	}

	controls { Ziva.controls(this) }

	ndef	{ | ... args | ^Ndef(this, *args) }
	sine	{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {SinOsc.ar(freq, phase).range(min,max) * amp})}
	tri		{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFTri.ar(freq, phase).range(min,max) * amp})}
	saw		{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFSaw.ar(freq, phase).range(min,max) * amp})}
	pulse	{ | freq, min=(-1), max=1, amp=1, width=0.5, phase=0 | ^Ndef(this, {LFPulse.ar(freq, phase, width).range(min,max) * amp})}
	noise0	{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFNoise0.ar(freq).range(min,max) * amp})}
	noise1	{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFNoise1.ar(freq).range(min,max) * amp})}
	noise2	{ | freq, min=(-1), max=1, amp=1, phase=0 | ^Ndef(this, {LFNoise2.ar(freq).range(min,max) * amp})}

	lfo { | args | ^Ndef(this, args) }
	// lfo { | args | ^Ndef(this, Ziva.oscillators[args[0]]).set(*args[1..]) }

	synth { | ... args |
		// ^this.play([Psynth(*args)]);
		this.play;
		^Ndef(this, Psynth(*args));
	}

	sample { | ... args |
		// ^this.play([Psample(*args)]);
		this.play;
		^Ndef(this, Psample(*args));
	}

	midi { | ... args |
		// ^this.play([Pmidi(*args)]);
		^Ndef(this, Pmidi(*args));
	}


	play { | ... args |
		if(Ndef(this).isPlaying.not) {
			if (Ndef(this).quant.isNil) {
				Ndef(this).quant = 1;
			};
			Ndef(this).fadeTime = 0.5;
			Ndef(this).clock = Ziva.clock;
			Ndef(this).play;
		} {
			Ndef(this).resume
		};

		// if(args.size > 0) {Ndef(this, Ppar(args))};

		^Ndef(this);
	}

	stop	{ ^Ndef(this).pause }
	pause	{ ^Ndef(this).pause }
	resume	{ ^Ndef(this).resume }
	mute	{ ^Ndef(this).pause }
	unmute	{ ^Ndef(this).resume }

	quant   { | q | Ndef(this).quant = q }

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

	fadeTime { | seconds | Ndef(this).fadeTime = seconds }

	fx { | effects |
		var fxNdef = this.prGetFxNdef;
		if( fxNdef.source.isNil && this != \all ) {
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
		(fxNdef.sources.size - effects.size).do{|i| fxNdef[i + effects.size + 1] = nil};

		// Now that we have an FX Ndef playing,
		// we don't want to play (monitor) the original any more
		Ndef(this).stop(fxNdef.fadeTime);

		^fxNdef;
	}

	set { | ... pairs |
		var fxNdef = this.prGetFxNdef;
		fxNdef.set(*pairs);
	}

	wet { | amt = 0.5 |
		var fxNdef = this.prGetFxNdef;
		amt = amt.clip(0,1);
		fxNdef.objects.indices.do{ arg index;
			if (index > 0) {
				var wetKey = ("wet" ++ index).asSymbol;
				fxNdef.set(wetKey, amt);
			}
		}
	}

	scramble { ^this.asString.scramble.asSymbol }

	rh { | args | ^this.prSynthOrSample.rh(args) }
	r { | args | ^this.rh(args) }
	scale { | scale | ^this.prSynthOrSample.scale(scale) }
	oct { | args | ^this.prSynthOrSample.oct(args) }
	freq { | args | ^this.prSynthOrSample.freq(args) }
	deg { | args | ^this.prSynthOrSample.deg(args) }
	dur { | args | ^this.prSynthOrSample.dur(args) }
	amp { | args | ^this.prSynthOrSample.amp(args) }
	leg { | args | ^this.prSynthOrSample.leg(args) }
	env { | args | ^this.prSynthOrSample.env(args) }
	perc { | args | ^this.prSynthOrSample.perc(args) }

	moogvcf{ | res=0.7 |  ^{arg sig; MoogVCF.ar(sig, Ndef(this), res, mul: 2)} }
	delay{ | dec |  ^{arg sig; sig + AllpassC.ar(sig, 2, Ndef(this), Ziva.ndef(dec) ? dec )} }
	lpf{ | res=0.1 |  ^{arg sig; RLPF.ar(sig, Ndef(this), Ziva.ndef(res) ? res)} }
	hpf{ | res=0.1 |  ^{arg sig; RHPF.ar(sig, Ndef(this), Ziva.ndef(res) ? res)} }
	moogvcf{ | res=0.7 |  ^{arg sig; MoogVCF.ar(sig, Ndef(this), Ziva.ndef(res) ? res, mul: 2)} }
	brown { | max=1.0, int=0.1 | ^Pbrown(Ndef(this), Ziva.ndef(max) ? max, int) }
	white { | max=1.0 | ^Pwhite(Ndef(this), Ziva.ndef(max) ? max) }
	ar{ | dec | ^[Ndef(this), Ziva.ndef(dec) ? dec] }
	perc{ | rel | ^[Ndef(this)] }

	fold { | max | ^{arg sig; if( max.isNil ) {sig.fold(Ndef(this) * (-1), Ndef(this))} {sig.fold(Ndef(\this) * (-1), max)} } }
}