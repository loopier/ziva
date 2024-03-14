+ Pattern {
    // unison { | voices=2, spread=0.001 | ^(this + Array.interpolation(voices, spread.neg, spread) ) }
    pwrap { arg lo, hi; ^Pwrap(this, lo, hi) }
    pn { arg repeats=1, key; ^Pn(this, repeats, key) }
    pgate { arg repeats, key; ^Pgate(this, repeats, key) }
    pdup { arg repeats=2; ^Pdup(repeats, this) }

}