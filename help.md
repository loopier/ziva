# Document Title
## utils
- `bj( hits, beats, offset=0 )`
- `bjr( hits, beats, offset=0 )`
- `brown( min, max=1, int=1 )`
- `white( min, max=1 )`
- `chop( size, chunks=16 )`
- `seed( number )`
- `unison( ###, spread=0.01 )`
## array
- `geom( ###, start=0, grow=1)`
- `series( ###, start=0, step=1)`
- `ser( ###, start=0, step=1)`
- `interp( ###, start=0.0, end=1.0)`
- `rnd( ###, min=0, max=1.0 )`
- `rnd2( ###, value=0 )`
- `linrnad( ###, min=0, max=1.0 )`
- `exprand( ###, min=0, max=1.0 )`
- `fib( ###, start=0, step=1 )`
## after Mercury's total-serialism
- `arp( ###, notesPerOctave=7)`
- `spread( ###, low=0.0, hi=0.1)`
- `dice( ###, sides=6)`
- `twelveTone{ ^(..12).scramble }`
## funcs
- `line( ###, end=1.0, dur=10 )`
- `sine( ###, min=(-1), max=1, amp=1, phase=0 )`
- `tri( ###, min=(-1), max=1, amp=1, phase=0 )`
- `saw( ###, min=(-1), max=1, amp=1, phase=0 )`
- `pulse( ###, min=(-1), max=1, amp=1, width=0.5, phase=0 )`
- `noise0( ###, min=(-1), max=1, amp=1, phase=0 )`
- `noise1( ###, min=(-1), max=1, amp=1, phase=0 )`
- `noise2( ###, min=(-1), max=1, amp=1, phase=0 )`
## fx
- `gain { ^{| in | in * this } }`
- `amp { ^{| in | in * this} }`
- `freereverb( ###, room=0.86, damp=0.3 | ^{| in )`
- `reverb( ###, room=0.86, damp=0.3 )`
- `gverb( ###, room | ^{| in )`
- `delay( ###, decay=0 | ^{| in )`
- `swdelay( ###, fb=0.7, dry=1, wet=1 | ^{|in)`
- `fbdelay( ###, fb=0.8 )`
- `lpf( ###, res = 1 | ^{| in )`
- `hpf( ###, res = 1 | ^{| in )`
- `bpf( ###, res = 1 | ^{| in )`
- `brf( ###, res = 1 | ^{| in )`
- `vcf( ###, res=0.7, mul=1 |  ^{| in )`
- `tremolo( ###, depth=0.3 | ^{| in )`
- `vibrato( ###, depth=0.3 | ^{| in )`
- `distort( ###, post, spread )`
- `techno {| x | ^{| in | RLPF.ar(in, SinOsc.ar(0.1).exprange(880,12000), 0.2)}}`
- `technosaw {| x | ^{| in | RLPF.ar(in, LFSaw.ar(0.2).exprange(880,12000), 0.2)}}`
- `cyberpunk {| x | ^{| in | Squiz.ar(in, 4.5, 5, 0.1)}}`
- `bitcrush {| x | ^{| in | Latch.ar(in, Impulse.ar(11000*0.5)).round(0.5 ** 6.7)}}`
- `antique {| x | ^{| in | LPF.ar(in, 1700) + Dust.ar(7, 0.6)}}`
- `crush( amount )`
- `chorus -- TODO`
- `chorus2 -- TODO`
- `compress -- TODO`
- `compress2( ###, below=1, above=0.5 )`
- `limiter( ###, dur=0.01 )`
- `limit( ###, dur=0.01 | ^{| in )`
- `distor( ###, post=1, spread=0.2 )`
- `afold( ###, neg=0.1, post=1 )` 
- `fold( threshold, post=1 )` 
    Symetric wave folding.
    - **threshold** - values > `threshold` will be folded.
    - **post** - gain after the folding.
