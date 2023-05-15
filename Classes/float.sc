+ Float {
	sine	{ | min=(-1), max=1, amp=1, phase=0 | ^{SinOsc.ar(this, phase).range(min,max) * amp}}
	tri		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFTri.ar(this, phase).range(min,max) * amp}}
	saw		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFSaw.ar(this, phase).range(min,max) * amp}}
	pulse	{ | min=(-1), max=1, amp=1, width=0.5, phase=0 | ^{LFPulse.ar(this, phase, width).range(min,max) * amp}}
	noise0	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise0.ar(this).range(min,max) * amp}}
	noise1	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise1.ar(this).range(min,max) * amp}}
	noise2	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise2.ar(this).range(min,max) * amp}}

	// delay{ | dec | (func: Ziva.fxDict[\delay], args:[this, dec]).know_(true) }
	// delay{ | dec | Ziva.fxDict[\delay], args:[this, dec]).know_(true) }
	delay 	{ | dec 	|  ^{arg sig; sig + AllpassC.ar(sig, 2, this, Ziva.ndef(dec) ? dec )} }
	lpf 	{ | res=0.1 |  ^{arg sig; RLPF.ar(sig, this, Ziva.ndef(res) ? res)} }
	hpf 	{ | res=0.1 |  ^{arg sig; RHPF.ar(sig, this, Ziva.ndef(res) ? res)} }
	moogvcf { | res=0.7 |  ^{arg sig; MoogVCF.ar(sig, this, Ziva.ndef(res) ? res, mul: 2)} }
	brown	{ | max=1.0, int=0.1 | ^Pbrown(this, Ziva.ndef(max) ? max, int) }
	white	{ | max=1.0 | ^Pwhite(this, Ziva.ndef(max) ? max) }
	adsr	{ | dec, sus, rel | ^[this, dec, sus, rel] }
	ar		{ | dec 	| ^[this, Ziva.ndef(dec) ? dec] }
	perc	{ | rel 	| ^[this] }
}