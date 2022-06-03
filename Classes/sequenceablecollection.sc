// Live coding in SuperCollider made easy.

// This is an extention of the Array class defining syntax
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

+ SequenceableCollection {
    ziva { |key, quant=1|
        // this.do {|x, i| x.debug(i)}.postln;
        // Ziva.play(this);
        ^Ziva.pdef(key, quant, this.flat);
    }

    chooseN { |size=4|
        ^({this.choose}.dup(size));
    }

    // concat { |arr|
    //     // var prev = [];
    //     // this.do{|n|
    //     //     prev = prev ++ n;
    //     // }
    //     // ^prev;
    //     ^(this++arr);
    // }

    // returns an array of variations of the given array following
    // the algorithm used to compose sextines in poetry.
    sextine {
        var sextine = List();
        var arr = this;
        sextine.add(arr);
        arr.size.do{
            var mid = (arr.size / 2).asInteger;
            arr = [arr[mid..].reverse, arr[..mid]].lace(arr.size);
            sextine.add(arr);
        };
        ^sextine.asArray;
    }

    // buleria { ^[\r,\r,1,\r,\r,1,\r,1,\r,1,\r,1] }


    rhythm { |rh, reverse=0|
        var pseq = Pseq(this,inf).asStream;
        rh = if(rh.isSymbol && Ziva.rhythmsDict.includesKey(rh)) {
            if(reverse == 0) { Ziva.rhythmsDict[rh] } { Ziva.rhythmsDict[rh].reverse };
        } {
            rh;
        };
        ^Array.fill(rh.flat.size, { |i|
            if(rh.flat[i] == \r) {
                \r;
            } {
                pseq.next;
            }
        });

    }

    /// \brief  convert rests to hits and viceversa
    inv { ^this.replace(\r, 0).invert.replace(0,\r) }

    // taken from https://www.thejazzpianosite.com/jazz-piano-lessons/jazz-genres/afro-cuban-latin-jazz/
    clave { |reverse=0| ^this.rhythm(\clave, reverse) }
    rumba { |reverse=0| ^this.rhythm(\rumba, reverse) }
    binaneth { |reverse=0| ^this.rhythm(\binaneth, reverse) }
    chitlins { |reverse=0| ^this.rhythm(\chitlins, reverse) }
    cascara { |reverse=0| ^this.rhythm(\cascara, reverse) }
    cencerro { |reverse=0| ^this.rhythm(\cencerro, reverse) }
    cencerru { |reverse=0| ^this.rhythm(\cencerru, reverse) }
    montuno { |reverse=0| ^this.rhythm(\montuno, reverse) }
    conga { |reverse=0| ^this.rhythm(\conga, reverse) }
    tumbao { |reverse=0| ^this.rhythm(\tumbao, reverse) }
    tumbau { |reverse=0| ^this.rhythm(\tumbao, reverse) }
    horace { |reverse=0| ^this.rhythm(\horace, reverse) }
    buleria { |reverse=0| ^this.rhythm(\buleria, reverse) }
    nine { |reverse=0| ^this.rhythm(\nine, reverse) }
    eleven { |reverse=0| ^this.rhythm(\eleven, reverse) }
    tonebank { |reverse=0| ^this.rhythm(\tonebank, reverse) }
    tracatrin { |reverse=0| ^this.rhythm(\tracatrin, reverse) }
    tracatron { |reverse=0| ^this.rhythm(\tracatron, reverse) }
    tracatrun { |reverse=0| ^this.rhythm(\tracatrun, reverse) }

    // add trailing rests
    // every { arg beats=4;
    //     ^this++(\r!beats).flat;
    // }

    // walk { arg size, list, stepPattern, directionPattern=1, startPos=0;
    //     // ^Array.fill(size, list.pwalk(stepPattern, directionPattern, startPos).iter);
    //     [size, list, stepPattern, directionPattern, startPos].collect(_.debug(_));
    // }

    pdef{ |key|
        ^Pdef(key, Ppar(this));
    }

    pseq { arg repeats=inf, offset=0;
        ^Pseq(this, repeats, offset);
    }

    pindex { arg indexPat, repeats=1;
        ^Pindex(this, indexPat, repeats);
    }

    pser { arg repeats=1, offset=0;
        ^Pser(this, repeats, offset);
    }

    pshuf { arg repeats=1;
        ^Pshuf(this, repeats);
    }

    prand { arg repeats=inf;
        ^Prand(this, repeats);
    }

    pxrand { arg repeats=inf;
        ^Pxrand(this, repeats);
    }

    pwrand  { arg weights, repeats=1;
        ^Pwrand(this, weights, repeats);
    }

    pfsm { arg repeats=1;
        ^Pfsm(this, repeats);
    }

    pdfsm { arg startState=0, repeats=1;
        ^Pdfsm(this, startState, repeats);
    }

    pswitch  { arg which=0;
        ^Pswitch(this, which);
    }

    pswitch1  { arg which=0;
        ^Pswitch1(this, which);
    }

    ptuple { arg repeats=1;
        ^Ptuple(this, repeats);
    }

    place { arg repeats=inf, offset=0;
        ^Place(this, repeats, offset);
    }

    ppatlace { arg repeats=1, offset=0;
        ^Ppatlace(this, repeats, offset);
    }

    pslide {  arg repeats = 1, len = 3, step = 1, start = 0, wrapAtEnd = true;
        ^Pslide(this, repeats, len, step, start, wrapAtEnd);
    }

    pwalk { arg stepPattern, directionPattern = 1, startPos = 0;
        ^Pwalk(this, stepPattern, directionPattern, startPos);
    }

	ppar { arg repeats=1;
		^Ppar(this, repeats);
	}

	ptpar { arg repeats=1;
		^Ptpar(this, repeats);
	}

	pline { arg repeats=1;
		^Pline(this, repeats);
	}
}
