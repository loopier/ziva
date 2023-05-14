+ Integer {
	sine	{ | ... args | ^this.asFloat.sine(*args)}
	tri		{ | ... args | ^this.asFloat.tri(*args)}
	saw		{ | ... args | ^this.asFloat.saw(*args)}
	pulse	{ | ... args | ^this.asFloat.pulse(*args)}
	noise0	{ | ... args | ^this.asFloat.noise0(*args)}
	noise1	{ | ... args | ^this.asFloat.noise1(*args)}
	noise2	{ | ... args | ^this.asFloat.noise2(*args)}


	bj { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset) }
	bjr { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset).replace(0,\r) }
	lpf { | res=0.1 | ^(func: Ziva.fxDict[\lpf], args:[this, res] ).know_(true) }
	brown { | max=1, int=1 | ^Pbrown(this, max, int) }
	white { | max=1 | ^Pwhite(this, max) }
	adsr{ | dec, sus, rel | ^[this, dec, sus, rel] }
	ar{ | dec | ^[this, dec] }
}