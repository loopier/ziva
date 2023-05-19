+ Integer {
	sine	{ | ... args | ^this.asFloat.sine(*args)}
	tri		{ | ... args | ^this.asFloat.tri(*args)}
	saw		{ | ... args | ^this.asFloat.saw(*args)}
	pulse	{ | ... args | ^this.asFloat.pulse(*args)}
	noise0	{ | ... args | ^this.asFloat.noise0(*args)}
	noise1	{ | ... args | ^this.asFloat.noise1(*args)}
	noise2	{ | ... args | ^this.asFloat.noise2(*args)}

	delay	{ | ... args | ^this.asFloat.delay(*args) }
	lpf		{ | ... args | ^this.asFloat.lpf(*args.debug("lfp")) }
	hpf		{ | ... args | ^this.asFloat.hpf(*args.debug("hfp")) }
	moogvcf	{ | ... args | ^this.asFloat.moogvcf(*args) }
	brown	{ | ... args | ^this.asFloat.brown(*args) }
	white	{ | ... args | ^this.asFloat.white(*args) }
	adsr	{ | ... args | ^this.asFloat.adsr(*args) }
	ar		{ | ... args | ^this.asFloat.ar(*args) }
	perc	{ | ... args | ^this.asFloat.perc(*args) }


	bj { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset) }
	bjr { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset).replace(0,\r) }
	brown { | max=1, int=1 | ^Pbrown(this, max, int) }
	white { | max=1 | ^Pwhite(this, max) }
	adsr{ | dec, sus, rel | ^[this, dec, sus, rel] }
	ar{ | dec | ^[this, dec] }

	chop { | chunks=16 | ^(..chunks).choosen(this) / chunks }
}