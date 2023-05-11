+ Ndef{
	prGetPbind {
		^this.source.list[0];
	}

	prSetPbindPair { | key, value |
		"%: %.% - %".format(this.key, key, value, this.prGetPbind)
		^Ndef(this.key, Pset(key, value, this.prGetPbind))
	}

	rh { | args |
		if( args.isSymbol ) {
			args = Ziva.rhythmsDict[args] ? args.asString;
		};

		if( args.isString ) { args = args.asBinaryDigits.flat.replace(0,\r) };

		args = args.debug("rhythm").pseq
		^this.prSetPbindPair(\r, args);
	}

	deg { | args |
		^this.prSetPbindPair(\degree, args);
	}

	dur { | args |
		var durs = Ziva.constants[args] ? args;
		^this.prSetPbindPair(\dur, durs);
	}

	fx { | args |
		// add to Ndef(thi.key)[...]
	}
}