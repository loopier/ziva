+ Ndef{
	// prGetPbind { | index = 0 |
	// 	if( this.source.class == Pbind ) { ^this.source };

	// 	^this.source.list[index];
	// }

	// prGetPairs { | index = 0 |
	// 	if( this.source.class == Pbind ) { ^this.source.patternpairs };

	// 	if( this.source.class == Ppar ) {
	// 		^this.source.patterns.list[index].pratternpairs;
	// 	}
	// }

	// prSetPbindPair { | key, value |
	// 	var pairs = this.source.list[0].patternpairs ++ [key.asSymbol, value];
	// 	^Ndef(this.key, Ppar([Pbind(*pairs)]));
	// }

	prChain { | ... args |
		^Ndef(this.key, Pchain(Pbind(*args), this.source));
	}

	dur { | args |
		if( args.isSymbol ) { args = Ziva.constants[args] ? args.asDurs.pseq };
		^this.prChain(\dur, args);
	}

	leg { | args |
		if( args.isSymbol ) { args = Ziva.ndef(args) ? Ziva.constants[args] ? args.asDurs };
		^this.prChain(\legato,  args);
	}

	env { | args |
		var bla = args.debug("env %".format(args.class));
		var env = switch( args.size,
			1, { ^this.perc(args) },
			2, { ^this.ar(args) },
			4, { ^this.adsr(args) }
		);
	}
	perc { | rel=1 | ^this.prChain(\atk, 0.01, \rel, Ziva.ndef(rel) ? rel, \legato, 0.01) }
	ar 	 { | env | ^this.prChain(\atk, env[0], \dec, env[1], \sus, 1, \rel, env[1]) }
	adsr { | env | ^this.prChain(\atk, env[0], \dec, env[1], \sus, env[2], \rel, env[3]) }

	amp { | args | ^this.prChain(\amp, Ziva.ndef(args) ? Ziva.constants[args] ? args) }

	rh { | args |
		args = Pseq(args.asRhythm.debug("% rhythm".format(this.key)), inf);
		^this.prSetPbindPair(\r, args);
	}

	oct { | args | ^this.prChain(\octave, args) }
	deg { | args |
		if( args.isSymbol ) { args = args.asDegrees };
		^this.prChain(\degree, args);
	}

	fx { | args |
		// add to Ndef(thi.key)[...]
	}

	lpf{ | res=0.1 |  ^{arg sig; RLPF.ar(sig, this, res)} }
	moogvcf{ | res=0.1 |  ^{arg sig; MoogVCF.ar(sig, this, res, mul: 2)} }
}