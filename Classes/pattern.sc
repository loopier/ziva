+ Pattern {
    unison { | voices=2, spread=0.01 | ^(this + Array.interpolation(voices, spread.neg, spread) ) }
}