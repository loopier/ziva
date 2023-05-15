// Live coding in SuperCollider made easy.

// This is an extention of the Pattern class defining syntax
// sugar for easier and faster live coding.

// (C) 2022 Roger Pibernat

// Ziva is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 2 of the License, or (at your
// option) any later version.

// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

+ Pattern {
	to { |name|
		Ndef(name.asSymbol, this).quant_(1);
	}

	bpm { |bpm|
		^Pchain(Pbind(\tempo, bpm/60), this);
	}

	pn {arg repeats=inf, key;
		^Pn(this, repeats, key);
	}

	pstutter {arg n;
		^Pstutter(n, this);
	}

	// pevery {arg n;
	// 	^Pseq([this] ++ (\r!n),inf);
	// }

	////////////////////////////////////////////////////////////////////////////
	// former PCHAIN extension
	////////////////////////////////////////////////////////////////////////////
	// map all methods that are not understood to a Pbind parameter
	doesNotUnderstand { |selector ...args|
		var value = Ziva.ndef(args[0].asSymbol) ? args[0];
		// selector.debug((this.class ++ " does not understand method").asString);

        // super.findRespondingMethodFor(selector);

		selector.debug("WARNING! Pchain doesNotUnderstand");
		selector.debug("WARNING! Trying to set synth parameter")
		// FIX: call super.doesNotUnderstand
		^Pchain(Pbind(selector, value), this);
	}

	>> { |track|
		^this.fx(track);
	}

	fx { |track|
		var sym = (\t++track).asSymbol;
		// Ziva.tracks[sym].debug(sym);
		^Pchain(Pbind(\out, Ziva.tracksDict[sym]), this);
	}

	fm { |track, amt=1|
		^Pchain(Pbind(\in, Ndef((\track_++track).asSymbol), \modamt, amt * 100), this);
	}

	cc { |cc, value| ^Pchain(Pbind(\ctlNum, cc, \control, value), this) }
	inst { |instrument| ^Pchain(Pbind(\instrument, instrument), this) }
	ins { |instrument| ^Pchain(Pbind(\instrument, instrument), this) }
	i { |instrument| ^Pchain(Pbind(\instrument, instrument), this) }

	scale { |name| ^Pchain(Pbind(\scale, Scale.at(name)), this) }
	deg { |value|
		if( value.isSymbol ) { value = value.debug("deg Value").asString.debug("deg String") };
		if( value.isString ) { value = Array.fill(value.size, {|i| value[i].asString.asHexIfPossible }).debug("deg Array").pseq };

		// ^Pchain(Pbind(\degree, value.debug("deg")), this);
		^Pset(\degree, value.debug("deg"), this);
	}
	oct { |value| ^Pchain(Pbind(\octave, value), this) }
	// pixi { |msg, durmul=1, oct=1|
	// 	var ixi = msg.ixi(oct:oct, durmul:durmul);
	// 	^Pchain(Pbind(
	// 		\degree, ixi.degs.pseq,
	// 		\dur, ixi.durs.pseq,
	// 	), this);
	// }

	once { |times = 1| ^Pchain(Pbind(\r, Pseq([1, \r], times)), this) }

	dur { | args |
		var durs = Ziva.constants[args] ? args;
		// ^Pchain(Pbind(\dur, durs), this);
		^Pset(\dur, durs, this);
	}

	amp { | val |
		var amp = Ziva.ndef(val) ? Ziva.constants[val] ? val;
		^Pchain(Pbind(\amp, amp), this);
	}

	bramp { ^Pchain(Pbind(\amp, Pbrown(0.1), this))}
	fadein { ^Pchain(Pbind(\amp, 0.3 * PLine(0, 1, 16)), this)}

	leg { | val | ^Pchain(Pbind(\legato, Ziva.constants[val] ? val), this) }

	env { | args |
		var env = switch( args.size,
			1, { ^this.perc(args) },
			2, { ^this.ar(args) },
			4, { ^this.adsr(args) }
		);
	}
	perc { | rel=1 | ^Pchain(Pbind(\atk, 0.01, \rel, Ziva.ndef(rel) ? rel, \legato, 0.01), this) }
	ar { | env | ^Pchain(Pbind(\atk, env[0], \dec, env[1], \sus, 1, \rel, env[1]), this) }
	adsr { | env | ^Pchain(Pbind(\atk, env[0], \dec, env[1], \sus, env[2], \rel, env[3]), this) }

	bjorklund {|k,n, rotate=0, scramble=false, sort=false, reverse=false|
		var bj = Bjorklund(k,n).replace(0,\r);
		if(scramble) {bj = bj.scramble} {};
		if(sort) {bj = bj.sort} {};
		if(reverse) {bj = bj.reverse} {};
		bj = bj.rotate(rotate);
		scramble.debug("scramble");
		sort.debug("sort");
		rotate.debug("rotate");
		reverse.debug("reverse");
		bj.debug("bj");
		^Pchain(Pbind(\bj, Pseq(bj, inf)), this);
	}

	bj { |k,n, rotate=0, scramble=false, sort=false, reverse=false|
		^this.bjorklund(k, n, rotate, scramble, sort, reverse);
	}

	upbeat { ^Pchain(Pbind(\r, Pseq([\r,1], inf)), this) }

	r { | args | ^this.rh(args) }
	rh { | args |
		if( args.isSymbol ) {
			args = Ziva.rhythmsDict[args] ? args.asString;
			// if( args.isArray) { args.debug("rhythm").pseq };
		};

		if( args.isString ) { args = args.asBinaryDigits.flat };
		if( args.isArray ) { args = args.flat.replace(0,\r).debug("rhythm").pseq }

		^Pchain( Pbind(\r, args), this );
	}

	stopin { |beats=1| ^Pchain(Pbind(\r, Pseq((1!beats),1)), this) }

	// rate { |rate| ^Pchain(Pbind(\rate, rate, \speed, this), this)}
	randspeeds { |size=8, speeds=#[-1,1,-0.5,0.5,2,-2]|
		var sp = Pseq(Array.fill(size, {speeds.choose}).debug("speeds "++this.name), inf);
		^Pchain(Pbind(\speed, sp), this);
	}

	chop { |size=8, chunks=16|
		var chopped = Pseq( Array.rand(size, 0, chunks-1).debug("chop "++this.name) / chunks, inf);
		^Pchain(Pbind(\start, chopped, \begin, chopped), this);
	}
}
