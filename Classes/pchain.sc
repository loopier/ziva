// Live coding in SuperCollider made easy.

// This is an extention of the Pchain class defining syntax
// sugar for easier and faster live coding.

// (C) 2022 Roger Pibernat

// Zivo is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 2 of the License, or (at your
// option) any later version.

// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

+Pchain {
	// map all methods that are not understood to a Pbind parameter
	doesNotUnderstand { |selector ...args|
		selector.debug("Pchain doesNotUnderstand");
		^Pchain(Pbind(selector, args[0]), this);
	}

	cc { |cc, value| ^Pchain(Pbind(\ctlNum, cc, \control, value), this) }
	inst { |instrument| ^Pchain(Pbind(\instrument, instrument), this) }
	ins { |instrument| ^Pchain(Pbind(\instrument, instrument), this) }
	i { |instrument| ^Pchain(Pbind(\instrument, instrument), this) }

	ixi { |msg| ^Pchain(Pbind(\note, msg.notes(3).pseq, this)) }
	deg { |value| ^Pchain(Pbind(\degree, value), this) }
	oct { |value| ^Pchain(Pbind(\octave, value), this) }

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

	ffff { ^Pchain(Pbind(\amp, 2), this)}
	fff { ^Pchain(Pbind(\amp, 1), this)}
	ff { ^Pchain(Pbind(\amp, 0.5), this)}
	f { ^Pchain(Pbind(\amp, 0.3), this)}
	p { ^Pchain(Pbind(\amp, 0.05), this)}
	pp { ^Pchain(Pbind(\amp, 0.02), this)}
	ppp { ^Pchain(Pbind(\amp, 0.01), this)}
	bramp { ^Pchain(Pbind(\amp, Pbrown(0.1), this))}
	fadein { ^Pchain(Pbind(\amp, 0.3 * PLine(0, 1, 16)), this)}

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

	stopin { |beats| ^Pchain(Pbind(\r, Pseq((1!beats),1)), this) }

	pedal { ^Pchain(Pbind(\legato, 4), this)}
	legato { |amt| ^Pchain(Pbind(\legato, amt ? 1.1), this)}
	tenuto { ^Pchain(Pbind(\legato, 1), this)}
	stacc { ^Pchain(Pbind(\legato, 0.5), this)}
	stass { ^Pchain(Pbind(\legato, 0.25), this)}
	pizz { ^Pchain(Pbind(\legato, 0.1), this)}

	rate { |rate| ^Pchain(Pbind(\rate, rate), this)}
	randrates { |size=8, rates=#[-1,1,-0.5,0.5,2,-2]| ^Pchain(Pbind(\rate, Pseq(Array.fill(size, {rates.choose}).debug("rates "++this.name), inf)), this)}

	chop { |size=8, chunks=16| ^Pchain(Pbind(\start, Pseq( Array.rand(size, 0, chunks-1).debug("chop "++this.name) / chunks, inf)), this)}

	kick { |pattern| ^Pchain(Pbind(\degree, 0, \octave, 3, \r, pattern), this)}
	sn { |pattern| ^Pchain(Pbind(\degree, 2, \octave, 3, \r, pattern), this)}


	gverb { |room = 0.1, size=0.3, wet=1, bus = 5|
		^Pchain(Pfxb(Pchain(Pbind(\out, bus), this), \gverb, \roomsize, room*100, \revtime, size*10, \mul, wet, \in, bus));
	}

	jpverb { |room = 1, size=1, damp=0, wet=1, bus = 7|
		^Pchain(Pfxb(Pchain(Pbind(\out, bus), this), \jpverb, \room, room.linlin(0.0,1.0,0.1,60), \size, size.linlin(0.0,1.0,0.5,5), \damp, damp, \mul, wet, \in, bus));
	}

	delay { |dt = 0.5, fb = 0.6, bus = 9|
		^Pchain(Pfxb(Pchain(Pbind(\out, [0, bus]), this), \delay, \fb, fb, \delayt, dt, \in, bus));
	}

	lfp { |freq = 440, rq = 0.5, bus = 11|
		^Pchain(Pfxb(Pchain(Pbind(\out, [0, bus]), this), \rlpf, \freq, freq, \rq, rq, \in, bus));
	}

	hfp { |freq = 440, rq = 0.5, bus = 11|
		^Pchain(Pfxb(Pchain(Pbind(\out, [0, bus]), this), \rhpf, \freq, freq, \rq, rq, \in, bus));
	}




	// rata { |repeats=4| ^Pchain(Pbind(\rate, Pn(Pchoose([0.5, 1, -1, 2, 4], 4, 4))), this)}
}
