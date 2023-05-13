+ Integer {
	bj { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset) }
	bjr { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset).replace(0,\r) }
	lpf { | res=0.1 | ^(func: Ziva.fxDict[\lpf], args:[this, res] ).know_(true) }
	// walk { | amt | ^Pseq() }
	brown { | max=1, int=1 | ^Pbrown(this, max, int) }
	white { | max=1 | ^Pwhite(this, max) }
	adsr{ | dec, sus, rel | ^[this, dec, sus, rel] }
	ar{ | dec | ^[this, dec] }
}