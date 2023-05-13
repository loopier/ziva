+ Integer {
	bj { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset) }
	bjr { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset).replace(0,\r) }
	lpf { | res=0.1 | ^(func: Ziva.fxDict[\lpf], args:[this, res] ).know_(true) }
}