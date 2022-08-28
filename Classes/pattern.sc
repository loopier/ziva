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
	// These methods work like the Pchain shortcut but they add and multiply
	// values instead of overriding them.
	// add Patterns
	// <+ { arg aPattern;
	// 	if (aPattern.isString) {
	// 		aPattern = Pixi(aPattern);
	// 	};
	// 	aPattern.patternpairs.do{|e, i|
	// 		if (e.class == Symbol) {
	// 			^Padd(e, aPattern.patternpairs[i+1], this);
	// 		} {
	// 			nil
	// 		}
	// 	};
	// }
	// // multiply Patterns
	// <* { arg aPattern;
	// 	if (aPattern.isString) {
	// 		aPattern = Pixi(aPattern);
	// 	};
	// 	aPattern.patternpairs.do{|e, i|
	// 		if (e.class == Symbol) {
	// 			^Pmul(e, aPattern.patternpairs[i+1], this);
	// 		} {
	// 			nil
	// 		}
	// 	};
	// }

	// overriding original
	// compose Patterns
	// <> { arg aPattern;
	// 	if (aPattern.isString) {
	// 		aPattern = Pixi(aPattern);
	// 	};
	// 	^Pchain(this, aPattern)
	// }

	// >> { |... pairs|
	// 	[this, pairs].debug(">>");
	// 	^Pfx(pairs);
	// }

	// fx { |... pairs|
	// 	[this, pairs].debug("fx");


	// pclump { arg n;
	// 	^Pclump(n, this);
	// }

	// pavaroh { arg aroh, avaroh, stepsPerOctave=12;
	// 	^Pavaroh(this, aroh, avaroh, stepsPerOctave);
	// }

	// prorate { arg proportion;
	// 	^Prorate(proportion, this);
	// }

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
		// selector.debug((this.class ++ " does not understand method").asString);

        // super.findRespondingMethodFor(selector);

		selector.debug("WARNING! Pchain doesNotUnderstand");
		selector.debug("WARNING! Trying to set synth parameter")
		// FIX: call super.doesNotUnderstand
		^Pchain(Pbind(selector, args[0]), this);
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
	deg { |value| ^Pchain(Pbind(\degree, value), this) }
	oct { |value| ^Pchain(Pbind(\octave, value), this) }
	ixi { |msg, oct=1| ^Pchain(Pbind(\note, msg.notes(oct).pseq), this)}

	once { |times = 1| ^Pchain(Pbind(\r, Pseq([1, \r], times)), this) }

	fastest { ^Pchain(Pbind(\dur, 1/8), this)}
	faster { ^Pchain(Pbind(\dur, 1/4), this)}
	fast { ^Pchain(Pbind(\dur, 1/2), this)}
	slow { ^Pchain(Pbind(\dur, 2), this)}
	slower { ^Pchain(Pbind(\dur, 4), this)}
	slowest { ^Pchain(Pbind(\dur, 8), this)}
	ultraslow { ^Pchain(Pbind(\dur, 16), this)}
	ultraslower { ^Pchain(Pbind(\dur, 32), this)}
	ultraslowest { ^Pchain(Pbind(\dur, 64), this)}

	lowest { ^Pchain(Pbind(\octave, 2), this)}
	lower { ^Pchain(Pbind(\octave, 3), this)}
	low { ^Pchain(Pbind(\octave, 4), this)}
	high { ^Pchain(Pbind(\octave, 6), this)}
	higher { ^Pchain(Pbind(\octave, 7), this)}
	highest { ^Pchain(Pbind(\octave, 8), this)}

	amp { |min, max|
		var amp;
		if (max.isNil) {
			amp = min;
		} {
			amp = Pwhite(min, max);
		};
		^Pchain(Pbind(\amp, amp), this);
	}
	ffff { ^Pchain(Pbind(\amp, 2), this)}
	fff { ^Pchain(Pbind(\amp, 1), this)}
	ff { ^Pchain(Pbind(\amp, 0.5), this)}
	f { ^Pchain(Pbind(\amp, 0.3), this)}
	p { ^Pchain(Pbind(\amp, 0.05), this)}
	pp { ^Pchain(Pbind(\amp, 0.02), this)}
	ppp { ^Pchain(Pbind(\amp, 0.01), this)}
	bramp { ^Pchain(Pbind(\amp, Pbrown(0.1), this))}
	fadein { ^Pchain(Pbind(\amp, 0.3 * PLine(0, 1, 16)), this)}

	perc { | rel=1 | ^Pchain(Pbind(\atk, 0.01, \rel, rel, \legato, 0.01), this) }
	ar { | atk=0.5, rel=0.5 | ^Pchain(Pbind(\atk, atk, \dec, rel, \sus, 1, \rel, rel), this) }
	adsr { | atk=0.01, dec=0.3, sus=0.5, rel=1.0 | ^Pchain(Pbind(\atk, atk, \dec, dec, \sus, sus, \rel, rel), this) }

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

	pattern { |pattern| ^Pchain(Pbind(\r, pattern), this)}
	buleria { |rotate=0| ^Pchain(Pbind(\r, [\r,\r,1,\r,\r,1,\r,1,\r,1,\r,1].rotate(rotate).pseq), this) }
	r { |pattern|
		var rhythm = pattern;
		if(pattern.isSymbol && Ziva.rhythmsDict.keys.includes(pattern)) {
			rhythm = Ziva.rhythmsDict[pattern].pseq;
		};
		if (pattern.isArray){
			rhythm = pattern.pseq;
		};
		^Pchain(Pbind(\r, rhythm), this);
	}
	// rh { |rhythm, fast=1|
		// var dur = if(Ziva.rhythmsDict.keys.includes(rhythm), {Ziva.rhythmsDict[rhythm].durs.pseq * (1/(fast.max(0.001)))}, {1});
		// // var rests = if(Ziva.rhythmsDict.includes(rhythm), {Ziva.rhythmsDict[rhythm].rests.pseq}, {1});
		// var sus = if(Ziva.rhythmsDict.keys.includes(rhythm), {Ziva.rhythmsDict[rhythm].sus.pseq}, {1});
		// rhythm.debug("rhythm");
		// (Ziva.rhythmsDict.includes(rhythm)).debug("rhythm");
		// dur.debug("durs");
		// sus.debug("sus");
		// ^Pchain(Pbind(\dur, dur, \legato, sus), this);
	// }

	stopin { |beats=1| ^Pchain(Pbind(\r, Pseq((1!beats),1)), this) }

	pedal { ^Pchain(Pbind(\legato, 4), this)}
	legato { |amt| ^Pchain(Pbind(\legato, amt ? 1.1), this)}
	leg { |amt| ^Pchain(Pbind(\legato, amt ? 1.1), this)}
	tenuto { ^Pchain(Pbind(\legato, 1), this)}
	stacc { ^Pchain(Pbind(\legato, 0.5), this)}
	stass { ^Pchain(Pbind(\legato, 0.25), this)}
	pizz { ^Pchain(Pbind(\legato, 0.1), this)}

	// rate { |rate| ^Pchain(Pbind(\rate, rate, \speed, this), this)}
	randspeeds { |size=8, speeds=#[-1,1,-0.5,0.5,2,-2]|
		var sp = Pseq(Array.fill(size, {speeds.choose}).debug("speeds "++this.name), inf);
		^Pchain(Pbind(\speed, sp), this);
	}

	chop { |size=8, chunks=16|
		var chopped = Pseq( Array.rand(size, 0, chunks-1).debug("chop "++this.name) / chunks, inf);
		^Pchain(Pbind(\start, chopped, \begin, chopped), this);
	}

	// kick { |pattern| ^Pchain(Pbind(\degree, 0, \octave, 3, \r, pattern), this)}
	// sn { |pattern| ^Pchain(Pbind(\degree, 2, \octave, 3, \r, pattern), this)}

	left { ^Pchain(Pbind(\pan, -1), this) }
	right { ^Pchain(Pbind(\pan, 1), this) }
	pingpong { ^Pchain(Pbind(\pan, Pseq([-1,1],inf)), this) }
	randpan  { ^Pchain(Pbind(\pan, Pwhite(-1.0)), this) }

	// gverb { |room = 0.1, size=0.3, wet=1, bus = 5|
	// 	^Pchain(Pfxb(Pchain(Pbind(\out, bus), this), \gverb, \roomsize, room*100, \revtime, size*10, \mul, wet, \in, bus));
	// }

	// jpverb { |room = 1, size=1, damp=0, wet=1, bus = 7|
	// 	^Pchain(Pfxb(Pchain(Pbind(\out, bus), this), \jpverb, \room, room.linlin(0.0,1.0,0.1,60), \size, size.linlin(0.0,1.0,0.5,5), \damp, damp, \mul, wet, \in, bus));
	// }

	// delay { |dt = 0.5, fb = 0.6, bus = 9|
	// 	^Pchain(Pfxb(Pchain(Pbind(\out, [0, bus]), this), \delay, \fb, fb, \delayt, dt, \in, bus));
	// }

	// lfp { |freq = 440, rq = 0.5, bus = 11|
	// 	^Pchain(Pfxb(Pchain(Pbind(\out, [0, bus]), this), \rlpf, \freq, freq, \rq, rq, \in, bus));
	// }

	// hfp { |freq = 440, rq = 0.5, bus = 11|
	// 	^Pchain(Pfxb(Pchain(Pbind(\out, [0, bus]), this), \rhpf, \freq, freq, \rq, rq, \in, bus));
	// }

	// rata { |repeats=4| ^Pchain(Pbind(\rate, Pn(Pchoose([0.5, 1, -1, 2, 4], 4, 4))), this)}

	/// \brief converts a string to a drum pattern
	drums { |str|
		var kits = "brscSlhLftHToyYxXBpiekKOzZ";
		var pat = Array.fill(str.size, { |i|
			var c = str.at(i);
			var k;
			if(c == $ ) {
				k = \r.debug("REST");
			}{
				k = kits.indexOf(c).debug(i);
			};
			k
		});
		pat.debug("drum pattern");
		^Pchain(Pbind(\note, pat.pseq), this);
	}
}
