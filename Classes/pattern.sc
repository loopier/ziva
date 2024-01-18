+ Pattern {
    unison { | voices=2, spread=0.001 | ^(this + Array.interpolation(voices, spread.neg, spread) ) }
    pwrap { arg lo, hi; ^Pwrap(this, lo, hi) }

}