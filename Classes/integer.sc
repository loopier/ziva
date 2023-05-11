+ Integer {
	bj { | beats | ^Bjorklund(this, beats) }
	lpf { | res=0.1 | ^(func: Ziva.fxDict[\lpf], args:[this, res] ).know_(true) }
}