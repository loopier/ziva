DrumKit {
	var <>name;
	var <>kick;
	var <>sn;
	var <>ch;
	var <>oh;
	var <>rim;
	var <>cym;
	var <>cr;
	var <>bell;
	var <>cl;
	var <>sh;
	var <>tom;

	*new {  arg name, kick=nil, sn=nil, ch=nil, oh=nil, rim=nil, cym=nil, cr=nil, bell=nil, cl=nil, sh=nil, tom=nil,
				kickch=2, snch=2, chch=2, ohch=2, rimch=2, cymch=2, crch=2, bellch=2, clch=2, shch=2, tomch=2;
		^super.new.init(
			name,
			kick, sn, ch, oh, rim, cym, cr, bell, cl, sh, tom,
			kickch, snch, chch, ohch, rimch, cymch, crch, bellch, clch, shch, tomch
		);
	}
	init {  arg name, kick=nil, sn=nil, ch=nil, oh=nil, rim=nil, cym=nil, cr=nil, bell=nil, cl=nil, sh=nil, tom=nil,
				kickch=2, snch=2, chch=2, ohch=2, rimch=2, cymch=2, crch=2, bellch=2, clch=2, shch=2, tomch=2;
		this.name = name;
		this.kick = Psample(kick, kickch, \dur, 0.25);
		this.sn = Psample(sn, snch, \dur, 0.25);
		this.ch = Psample(ch, chch, \dur, 0.25);
		this.oh = Psample(oh, ohch, \dur, 0.25);
		this.rim = Psample(rim, rimch, \dur, 0.25);
		this.cym = Psample(cym, cymch, \dur, 0.25);
		// this.cr = Psample(cr, crch, \dur, 0.25);
		this.bell = Psample(bell, bellch, \dur, 0.25);
		this.cl = Psample(cl, clch, \dur, 0.25);
		this.sh = Psample(sh, shch, \dur, 0.25);
		// this.tom = Psample(tom, tomch);
	}

	rhythm { arg drumpattern;
		^Pdef(this.name, Ppar([
			this.kick.r(DrumPattern.at(drumpattern).kick.pseq),
			this.sn.r(DrumPattern.at(drumpattern).sn.pseq),
			this.ch.r(DrumPattern.at(drumpattern).ch.pseq),
			this.oh.r(DrumPattern.at(drumpattern).oh.pseq),
			this.rim.r(DrumPattern.at(drumpattern).rim.pseq),
			this.cym.r(DrumPattern.at(drumpattern).cym.pseq),
			// this.cr.r(DrumPattern.at(drumpattern).cr.pseq),
			this.bell.r(DrumPattern.at(drumpattern).bell.pseq),
			this.cl.r(DrumPattern.at(drumpattern).cl.pseq),
			this.sh.r(DrumPattern.at(drumpattern).sh.pseq),
			// this.tom.r(DrumPattern.at(drumpattern).tom.pseq),
		]));
	}

	r { arg drumpattern; ^this.rhythm(drumpattern) }

	// amps { arg  kick=nil, sn=nil, ch=nil, oh=nil, rim=nil, cym=nil, cr=nil, bell=nil, cl=nil, sh=nil, tom=nil;
	// 	if(kick.isNil.not) {this.kick.amp(kick.debug("kick"))}{};
	// 	if(sn.isNil.not) {this.sn.amp(sn)}{};
	// 	if(ch.isNil.not) {this.ch.amp(ch)}{};
	// 	if(oh.isNil.not) {this.oh.amp(oh)}{};
	// 	if(rim.isNil.not) {this.rim.amp(rim)}{};
	// 	if(cym.isNil.not) {this.cym.amp(cym)}{};
	// 	if(bell.isNil.not) {this.bell.amp(bell)}{};
	// 	if(cl.isNil.not) {this.cl.amp(cl)}{};
	// 	if(sh.isNil.not) {this.sh.amp(sh)}{};
	// 	if(tom.isNil.not) {this.tom.amp(tom)}{};
	// 	^Pdef(this.name);
	// }
}