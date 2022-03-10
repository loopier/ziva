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
	<+ { arg aPattern;
		if (aPattern.isString) {
			aPattern = Pixi(aPattern);
		};
		aPattern.patternpairs.do{|e, i|
			if (e.class == Symbol) {
				^Padd(e, aPattern.patternpairs[i+1], this);
			} {
				nil
			}
		};
	}
	// multiply Patterns
	<* { arg aPattern;
		if (aPattern.isString) {
			aPattern = Pixi(aPattern);
		};
		aPattern.patternpairs.do{|e, i|
			if (e.class == Symbol) {
				^Pmul(e, aPattern.patternpairs[i+1], this);
			} {
				nil
			}
		};
	}

	// overriding original
	// compose Patterns
	<> { arg aPattern;
		if (aPattern.isString) {
			aPattern = Pixi(aPattern);
		};
		^Pchain(this, aPattern)
	}

	// >> { |... pairs|
	// 	[this, pairs].debug(">>");
	// 	^Pfx(pairs);
	// }

	// fx { |... pairs|
	// 	[this, pairs].debug("fx");
	// }

	pclump { arg n;
		^Pclump(n, this);
	}

	pavaroh { arg aroh, avaroh, stepsPerOctave=12;
		^Pavaroh(this, aroh, avaroh, stepsPerOctave);
	}

	prorate { arg proportion;
		^Prorate(proportion, this);
	}

	pn {arg repeats=inf, key;
		^Pn(this, repeats, key);
	}

	pstutter {arg n;
		^Pstutter(n, this);
	}

	pevery {arg n;
		^Pseq([this] ++ (\r!n),inf);
	}
}
