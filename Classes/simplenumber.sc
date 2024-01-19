+ Number {
	// utils
	bj { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset) }
	bjr { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset).replace(0,\r) }
	brown { | max=1, int=1 | ^Pbrown(this, max, int) }
	white { | max=1 | ^Pwhite(this, max) }
	chop { | chunks=16 | ^(..chunks).choosen(this) / chunks }

	seed { thisThread.randSeed = this }
    unison { | spread=0.01 | ^Array.interpolation(this, spread.neg, spread) }

	// array
	geom{ |start=0, grow=1| ^Array.geom(this, start, grow) }
	series { |start=0, step=1| ^Array.series(this, start, step) }
	ser { |start=0, step=1| ^this.series(this, start, step) }
	interp { |start=0.0, end=1.0| ^Array.interpolation(this, start, end) }
	rnd { | min=0, max=1.0 | ^Array.rand(this, min, max) }
	rnd2 { | value=0 | ^Array.rand2(this, value) }
	linrnad { | min=0, max=1.0 | ^Array.linrand(this, min, max) }
	exprand { | min=0, max=1.0 | ^Array.exprand(this, min, max) }
	fib { | start=0, step=1 | ^Array.vib(this, start, step) }

	// after Mercury's total-serialism
	arp { |notesPerOctave=7| ^([0,2,4].dup(this) + ((..this-1) * notesPerOctave)).flat} // this = octave
	spread { |low=0.0, hi=0.1| ^interp(this, low, hi) }
	dice { |sides=6| ^(1..sides).choosen(this) }
	twelveTone{ ^(..12).scramble }

	// funcs
	line	{ | end=1.0, dur=10 | ^{Line.ar(this, end, dur)}}
	sine	{ | min=(-1), max=1, amp=1, phase=0 | ^{SinOsc.ar(this, phase).range(min,max) * amp}}
	tri		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFTri.ar(this, phase).range(min,max) * amp}}
	saw		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFSaw.ar(this, phase).range(min,max) * amp}}
	pulse	{ | min=(-1), max=1, amp=1, width=0.5, phase=0 | ^{LFPulse.ar(this, phase, width).range(min,max) * amp}}
	noise0	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise0.ar(this).range(min,max) * amp}}
	noise1	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise1.ar(this).range(min,max) * amp}}
	noise2	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise2.ar(this).range(min,max) * amp}}

	// fx
	gain { ^{| in | in * this } }
	// amp { ^{| in | in * this} }
	freereverb {| room=0.86, damp=0.3 | ^{| in | (in*0.6) + FreeVerb.ar(in, this, room, damp)} }
	reverb {| room=0.86, damp=0.3 | ^this.freeverb(room, damp) }
	gverb {| room | ^{| in | HPF.ar(GVerb.ar(in, roomsize:20, revtime:2, damping:0.3, inputbw:0.02, drylevel:0.7, earlyreflevel:0.7, taillevel:0.5), 100)}}
	delay {| decay=0 | ^{| in | AllpassC.ar(in, min(this,4), this, decay)}}
	swdelay {| fb=0.7, dry=1, wet=1 | ^{|in| SwitchDelay.ar(in, dry, wet, this, fb )}}
	fbdelay {| fb=0.8 |
		^{| in |
			var local;
			// read feedback , add to source
			local = LocalIn.ar(2) + in;
			// delay sound
			local = DelayN.ar(local, max(this, 0.2), this);
			// reverse channels to give ping pong effect, apply decay factor
			// LocalOut.ar(local.reverse * fb);
			LocalOut.ar(local * fb);
			local
		}
	}
	lpf {| res = 1 | ^{| in | RLPF.ar(in, max(this,1), res)}}
	hpf {| res = 1 | ^{| in | RHPF.ar(in, max(this,1), res)}}
	bpf {| res = 1 | ^{| in | BPF.ar(in, max(this,1), res)}}
	brf {| res = 1 | ^{| in | BRF.ar(in, max(this,1), res)}}
	vcf { | res=0.7, mul=1 |  ^{| in | MoogVCF.ar(in, this, res, mul: mul)} }
	tremolo {| depth=0.3 | ^{| in | in * SinOsc.ar(this, 0, depth, 0)}}
	vibrato {| depth=0.3 | ^{| in | PitchShift.ar(in, 0.008, SinOsc.ar(this, 0, depth, 1))}}
	// techno {| x | ^{| in | RLPF.ar(in, SinOsc.ar(0.1).exprange(880,12000), 0.2)}}
	// technosaw {| x | ^{| in | RLPF.ar(in, LFSaw.ar(0.2).exprange(880,12000), 0.2)}}
	// distort {| x | ^{| in | (3111.33*in.distort/(1+(2231.23*in.abs))).distort*0.02}}
	// cyberpunk {| x | ^{| in | Squiz.ar(in, 4.5, 5, 0.1)}}
	// bitcrush {| x | ^{| in | Latch.ar(in, Impulse.ar(11000*0.5)).round(0.5 ** 6.7)}}
	// antique {| x | ^{| in | LPF.ar(in, 1700) + Dust.ar(7, 0.6)}}
	crush {^{| in | in.round(0.5 ** (this-1));}}
	// chorus {^{| in | Mix.fill(7, {
	// 	var maxdelaytime = rrand(0.005,0.02);
	// 	DelayC.ar(in, maxdelaytime, LFNoise1.kr(Rand(4.5,10.5),0.25*maxdelaytime,0.75*maxdelaytime) );
	// })}}
	// chorus2 {^{| in | Mix.fill(7, {
	// 	var maxdelaytime= rrand(0.005,0.02);
	// 	Splay.ar(Array.fill(4,{
	// 		var maxdelaytime= rrand(0.005,0.02);
	// 		var del = DelayC.ar(in[0], maxdelaytime,LFNoise1.kr(Rand(0.1,0.6),0.25*maxdelaytime,0.75*maxdelaytime));
	// 		// LinXFade2.ar(in, del, \chorusamt.kr(0.0).linlin(0.0,1.0, -1.0,1.0))
	// 		LinXFade2.ar(in, del, this.linlin(0.0,1.0, -1.0,1.0));
	// 		del;
	// 	}))
	// })}}
	compress {^{| in | Compander.ar(4*(in),in,0.4,1,4,mul:this)}}
	compress2{ | below=1, above=0.5 | ^{arg in; Compander.ar(in, in, this, below, above, 0.01, 0.01) }}
	limiter {| dur=0.01 | ^limit(dur)}
	limit {| dur=0.01 | ^{| in | Limiter.ar(in, this, dur)}}
}

+ Float {
	fold {| max=1 | ^{| in | LeakDC.ar( in.fold(this, max) )}}
}

// + Integer {
// 	// utils
// 	bj { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset) }
// 	bjr { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset).replace(0,\r) }
// 	brown { | max=1, int=1 | ^Pbrown(this, max, int) }
// 	white { | max=1 | ^Pwhite(this, max) }
// 	chop { | chunks=16 | ^(..chunks).choosen(this) / chunks }

// 	// functions
// 	line	{ | end=10, dur=10 | ^{Line.ar(this, end, dur)}}
// 	sine	{ | min=(-1), max=1, amp=1, phase=0 | ^{SinOsc.ar(this, phase).range(min,max) * amp}}
// 	tri		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFTri.ar(this, phase).range(min,max) * amp}}
// 	saw		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFSaw.ar(this, phase).range(min,max) * amp}}
// 	pulse	{ | min=(-1), max=1, amp=1, width=0.5, phase=0 | ^{LFPulse.ar(this, phase, width).range(min,max) * amp}}
// 	noise0	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise0.ar(this).range(min,max) * amp}}
// 	noise1	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise1.ar(this).range(min,max) * amp}}
// 	noise2	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise2.ar(this).range(min,max) * amp}}

// 	// fx
// 	freereverb {| room=0.86, damp=0.3 | ^{| in | (in*0.6) + FreeVerb.ar(in, this, room, damp)} }
// 	reverb {| room=0.86, damp=0.3 | ^this.freeverb(room, damp) }
// 	gverb {| room | ^{| in | HPF.ar(GVerb.ar(in, roomsize:20, revtime:2, damping:0.3, inputbw:0.02, drylevel:0.7, earlyreflevel:0.7, taillevel:0.5), 100)}}
// 	delay {| decay=0 | ^{| in | AllpassC.ar(in, min(this,4), this, decay )}}
// 	swdelay {| fb=0.7, dry=1, wet=1 | ^{|in| SwitchDelay.ar(in, dry, wet, this, fb )}}
// 	fbdelay {| fb=0.8 |
// 		^{| in |
// 			var local;
// 			// read feedback , add to source
// 			local = LocalIn.ar(2) + in;
// 			// delay sound
// 			local = DelayN.ar(local, max(this, 0.2), this);
// 			// reverse channels to give ping pong effect, apply decay factor
// 			// LocalOut.ar(local.reverse * fb);
// 			LocalOut.ar(local * fb);
// 			local
// 		}
// 	}
// 	lpf {| res = 1 | ^{| in | RLPF.ar(in, max(this,1), res)}}
// 	hpf {| res = 1 | ^{| in | RHPF.ar(in, max(this,1), res)}}
// 	bpf {| res = 1 | ^{| in | BPF.ar(in, max(this,1), res)}}
// 	brf {| res = 1 | ^{| in | BRF.ar(in, max(this,1), res)}}
// 	vcf { | res=0.7, mul=1 |  ^{| in | MoogVCF.ar(in, this, res, mul: mul)} }
// 	tremolo {| depth=0.3 | ^{| in | in * SinOsc.ar(this, 0, depth, 0)}}
// 	vibrato {| depth=0.3 | ^{| in | PitchShift.ar(in, 0.008, SinOsc.ar(this, 0, depth, 1))}}
// 	crush {^{| in | in.round(0.5 ** (this-1));}}
// 	// chorus {^{| in | Mix.fill(7, {
// 	// 	var maxdelaytime = rrand(0.005,0.02);
// 	// 	DelayC.ar(in, maxdelaytime,LFNoise1.kr(Rand(4.5,10.5),0.25*maxdelaytime,0.75*maxdelaytime) );
// 	// })}}
// 	// chorus2 {^{| in | Mix.fill(7, {
// 	// 	var maxdelaytime= rrand(0.005,0.02);
// 	// 	Splay.ar(Array.fill(4,{
// 	// 		var maxdelaytime= rrand(0.005,0.02);
// 	// 		var del = DelayC.ar(in[0], maxdelaytime,LFNoise1.kr(Rand(0.1,0.6),0.25*maxdelaytime,0.75*maxdelaytime));
// 	// 		// LinXFade2.ar(in, del, this.linlin(0.0,1.0, -1.0,1.0));
// 	// 		del;
// 	// 	}))
// 	// })}}
// 	// compress {^{| in | Compander.ar(4*(in),in,0.4,1,4,mul:this)}}
// 	// limit {| dur=0.01 | ^{| in | Limiter(in, this, dur)}}
// 	fold {| max=1 | ^{| in | LeakDC.ar( in.fold(this, max) )}}
// }

// Ziva 1.0
// + Float {
// 	sine	{ | min=(-1), max=1, amp=1, phase=0 | ^{SinOsc.ar(this, phase).range(min,max) * amp}}
// 	tri		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFTri.ar(this, phase).range(min,max) * amp}}
// 	saw		{ | min=(-1), max=1, amp=1, phase=0 | ^{LFSaw.ar(this, phase).range(min,max) * amp}}
// 	pulse	{ | min=(-1), max=1, amp=1, width=0.5, phase=0 | ^{LFPulse.ar(this, phase, width).range(min,max) * amp}}
// 	noise0	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise0.ar(this).range(min,max) * amp}}
// 	noise1	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise1.ar(this).range(min,max) * amp}}
// 	noise2	{ | min=(-1), max=1, amp=1, phase=0 | ^{LFNoise2.ar(this).range(min,max) * amp}}

// 	// delay{ | dec | (func: Ziva.fxDict[\delay], args:[this, dec]).know_(true) }
// 	// delay{ | dec | Ziva.fxDict[\delay], args:[this, dec]).know_(true) }
// 	delay 	{ | dec 	|  ^{arg sig; sig + AllpassC.ar(sig, 2, this, Ziva.ndef(dec) ? dec )} }
// 	lpf 	{ | res=0.1 |  ^{arg sig; RLPF.ar(sig, this, Ziva.ndef(res) ? res)} }
// 	hpf 	{ | res=0.1 |  ^{arg sig; RHPF.ar(sig, this, Ziva.ndef(res) ? res)} }
// 	moogvcf { | res=0.7 |  ^{arg sig; MoogVCF.ar(sig, this, Ziva.ndef(res) ? res, mul: 2)} }
// 	brown	{ | max=1.0, int=0.1 | ^Pbrown(this, Ziva.ndef(max) ? max, int) }
// 	white	{ | max=1.0 | ^Pwhite(this, Ziva.ndef(max) ? max) }
// 	adsr	{ | dec, sus, rel | ^[this, dec, sus, rel] }
// 	ar		{ | dec 	| ^[this, Ziva.ndef(dec) ? dec] }
// 	perc	{ | rel 	| ^[this] }
// 	fold	{ | max 	| ^{arg sig; if(max.isNil) {sig.fold(this.neg, this)} {sig.fold(this,max)} } }
// 	// compress{ | amt=1	| ^{arg sig; Compander.ar(4*(sig),sig,0.4,1,4,mul:amt)} }
// 	compress{ | threshold=0.4, below=1, above=0.5 | ^{arg sig; Compander.ar(sig, sig, threshold, below, above, 0.01, 0.01) }}
// }
//
// + Integer {
// 	sine	{ | ... args | ^this.asFloat.sine(*args)}
// 	tri		{ | ... args | ^this.asFloat.tri(*args)}
// 	saw		{ | ... args | ^this.asFloat.saw(*args)}
// 	pulse	{ | ... args | ^this.asFloat.pulse(*args)}
// 	noise0	{ | ... args | ^this.asFloat.noise0(*args)}
// 	noise1	{ | ... args | ^this.asFloat.noise1(*args)}
// 	noise2	{ | ... args | ^this.asFloat.noise2(*args)}

// 	delay	{ | ... args | ^this.asFloat.delay(*args) }
// 	lpf		{ | ... args | ^this.asFloat.lpf(*args.debug("lpf")) }
// 	hpf		{ | ... args | ^this.asFloat.hpf(*args.debug("hpf")) }
// 	moogvcf	{ | ... args | ^this.asFloat.moogvcf(*args) }
// 	brown	{ | ... args | ^this.asFloat.brown(*args) }
// 	white	{ | ... args | ^this.asFloat.white(*args) }
// 	adsr	{ | ... args | ^this.asFloat.adsr(*args) }
// 	ar		{ | ... args | ^this.asFloat.ar(*args) }
// 	perc	{ | ... args | ^this.asFloat.perc(*args) }


// 	bj { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset) }
// 	bjr { | beats, offset=0 | ^Bjorklund(this, beats).rotate(offset).replace(0,\r) }
// 	brown { | max=1, int=1 | ^Pbrown(this, max, int) }
// 	white { | max=1 | ^Pwhite(this, max) }
// 	adsr{ | dec, sus, rel | ^[this, dec, sus, rel] }
// 	ar{ | dec | ^[this, dec] }

// 	chop { | chunks=16 | ^(..chunks).choosen(this) / chunks }
// }