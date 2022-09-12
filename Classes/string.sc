// Live coding in SuperCollider made easy.

// This is an extention of the String class defining syntax
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

+ String {
	// \brief convert string to array of notes
	digits { |octaves=1, degreesPerOctave=7, durmul=1|
		// replace numbers for letters to keep their right value when converting
		var str = this.letters;

		// replace nil by \r
		// "a" default value is 10, the whole sequence needs to be
		// offset so "a" === 0

		// old version
		// var digits = str.digit.collect(_ ?? {\r}) - 10 ;
		// var modulo = degreesPerOctave ?? digits.copy.sort.last;
		// ^digits.mod(modulo * octaves);

		var notes = List();
		var durs = List();
		var dur = 1;
		var modulo;
		// default dur to half a beat
		durmul = durmul / 2;

		str.do{ | x, i |
			case
			{x.asString.digit[0].isNil.not} {
				notes.add(x.asString.digit[0] - 10);
				durs.add(dur);
				dur = 1;
			}
			{x == $_} { dur = dur + 1 }
			{ // any  other character is a rest
				durs.add(dur);
				notes.add(\r);
				dur = 1;
			};
		};

		notes = notes.asArray;
		modulo = degreesPerOctave ?? notes.copy.sort.last;
		notes = notes.mod(modulo * octaves);

		// durations are offset by 1 position. To align them to the notes we
		// need to shift them to the left, discarding the initial value
		durs.add(dur).removeAt(0);
		durs = durs.asArray * durmul;
		^(degs: notes.debug("notes"), durs: durs.debug("durs")).know_(true);
	}

	// \brief convert numbers in a string to letters
	letters {
		^this
		.tr($0, $a)
		.tr($1, $b)
		.tr($2, $c)
		.tr($3, $d)
		.tr($4, $e)
		.tr($5, $f)
		.tr($6, $g)
		.tr($7, $h)
		.tr($8, $i)
		.tr($9, $j);
	}

	degrees { |octaves=1, degreesPerOctave=7|
		^this.digits(octaves, degreesPerOctave).degs;
	}

	// \brief convert string to array of notes
	notes { |octaves=1, degreesPerOctave=7|
		^this.digits(octaves, degreesPerOctave).degs;
	}

	// \brief convert string to array of notes
	durs { |octaves=1, degreesPerOctave=7, mul=1|
		^this.digits(octaves, degreesPerOctave, mul).durs;
	}

	// \brief convert string to array of notes
	ixi { |octaves=1, degreesPerOctave=7, durmul=1|
		^this.digits(octaves, degreesPerOctave, durmul);
	}

	// \brief convert string to a sequence of notes
	// pixi { arg octaves=1, repeats=inf;
	// 	// ^Pixi(this, repeats);
	// 	^this.digits(octaves).pseq(repeats);
	// }

	// \brief convert string to pseq
	pseq{ arg repeats=1, offset=0;
		^Pseq(this, repeats, offset);
	}

	// doesNotUnderstand { |selector ... args|
	// 	^("this.asArray."++selector++"(args)").interpret ?? { ^super.doesNotUnderstand(selector, args) }
	// }
}