+ Float {
	// freereverb {| room=0.86, damp=0.3 | ^{| in | (in*0.6) + FreeVerb.ar(in, this, room, damp)} }
	// reverb {| room=0.86, damp=0.3 | ^this.freeverb(room, damp) }
	// gverb {| room | ^{| in | HPF.ar(GVerb.ar(in, roomsize:20, revtime:2, damping:0.3, inputbw:0.02, drylevel:0.7, earlyreflevel:0.7, taillevel:0.5), 100)}}
	// gverbL {| x | ^{| in | HPF.ar(GVerb.ar(in, roomsize:30, revtime:3, damping:0.3, inputbw:0.5, drylevel:0.5, earlyreflevel:0.5, taillevel:0.5), 100)}}
	// gverbXL {| x | ^{| in | HPF.ar(GVerb.ar(in, roomsize:40, revtime:4, damping:0.2, inputbw:0.5, drylevel:0.2, earlyreflevel:0.3, taillevel:0.5), 100)}}
	delay {| decay=0 | ^{| in | in + AllpassC.ar(in, 2, this, decay )}}
	// lpfS {| x | ^{| in | LPF.ar(in, \lcutoff.kr(3000))}}
	// lpf {| x | ^{| in | lcutoff=1000; RLPF.ar(in, lcutoff, \lres.kr(1.0))}}
	// lpfL {| x | ^{| in | LPF.ar(in, \lcutoff.kr(50))}}
	// hpfS {| x | ^{| in | HPF.ar(in, \hcutoff.kr(50))}}
	// hpf {| x | ^{| in | RHPF.ar(in, \hcutoff.kr(1000), \hres.kr(1.0))}}
	// hpfL {| x | ^{| in | HPF.ar(in, \hcutoff.kr(1500))}}
	// bpf {| x | ^{| in | BPF.ar(in, \bcutoff.kr(1500), \bres.kr(1.0))}}
	// tremolo {| x | ^{| in | (in * SinOsc.ar(2.1, 0, 5.44, 0))*0.5}}
	// vibrato {| x | ^{| in | PitchShift.ar(in, 0.008, SinOsc.ar(2.1, 0, 0.11, 1))}}
	// techno {| x | ^{| in | RLPF.ar(in, SinOsc.ar(0.1).exprange(880,12000), 0.2)}}
	// technosaw {| x | ^{| in | RLPF.ar(in, LFSaw.ar(0.2).exprange(880,12000), 0.2)}}
	// distort {| x | ^{| in | (3111.33*in.distort/(1+(2231.23*in.abs))).distort*0.02}}
	// cyberpunk {| x | ^{| in | Squiz.ar(in, 4.5, 5, 0.1)}}
	// bitcrush {| x | ^{| in | Latch.ar(in, Impulse.ar(11000*0.5)).round(0.5 ** 6.7)}}
	// antique {| x | ^{| in | LPF.ar(in, 1700) + Dust.ar(7, 0.6)}}
	// crush {| x | ^{| in | in.round(0.5 ** (\crush.kr(6.6)-1));}}
	// chorus {| x | ^{| in | Mix.fill(7, {
	// 	var maxdelaytime= rrand(0.005,0.02)
	// 	DelayC.ar(in, maxdelaytime,LFNoise1.kr(Rand(4.5,10.5),0.25*maxdelaytime,0.75*maxdelaytime) )
	// })}}
	// chorus2 {| x | ^{| in | Mix.fill(7, {
	// 	var maxdelaytime= rrand(0.005,0.02)
	// 	Splay.ar(Array.fill(4,{
	// 		var maxdelaytime= rrand(0.005,0.02)
	// 		var del = DelayC.ar(in[0], maxdelaytime,LFNoise1.kr(Rand(0.1,0.6),0.25*maxdelaytime,0.75*maxdelaytime))
	// 		// LinXFade2.ar(in, del, \chorusamt.kr(0.0).linlin(0.0,1.0, -1.0,1.0))
	// 		del
	// 	}))
	// })}}
	// compress {| x | ^{| in | Compander.ar(4*(in),in,0.4,1,4,mul:\compressamt.kr(1))}}
	// fold {| x | ^{| in | in.fold(\foldmin.kr(0.01), \foldmax.kr(1))}}
}