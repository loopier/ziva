// A class to control Zynaddsubfx from SC via OSC.
// This is not exhaustive (yet). Just exposing some useful parameters to be live coded
Zynaddsubfx {
	classvar <>osc;
	classvar <>oscInterfaceDict;

	*initClass {
		Zynaddsubfx.osc = NetAddr("localhost", 4001);

		Zynaddsubfx.initDict;
		Zynaddsubfx.initEventType;

	}

	*initEventType {
		Event.addEventType(\zynaddsubfx, {
			// Zynaddsubfx.pan(~chan, ~pan.linlin(-1.0,1.0, 0,127));
			Zynaddsubfx.eventParamToMsg(~chan, \pan, ~pan);
			// ctl
			Zynaddsubfx.eventParamToMsg(~chan, \modwheel, ~modwheel);
			// global
			// amp
			Zynaddsubfx.eventParamToMsg(~chan, \volume, ~amp);
			// filter
			Zynaddsubfx.eventParamToMsg(~chan, \cutoff, ~cutoff);
			Zynaddsubfx.eventParamToMsg(~chan, \res, ~res);
			// filter env
			Zynaddsubfx.eventParamToMsg(~chan, \fatk, ~fatk);
			Zynaddsubfx.eventParamToMsg(~chan, \fatkl, ~fatkl);
			Zynaddsubfx.eventParamToMsg(~chan, \fdec, ~fdec);
			Zynaddsubfx.eventParamToMsg(~chan, \fdecl, ~fdecl);
			Zynaddsubfx.eventParamToMsg(~chan, \frel, ~frel);
			Zynaddsubfx.eventParamToMsg(~chan, \frell, ~frell);
			// filter lfo
			Zynaddsubfx.eventParamToMsg(~chan, \flfofq, ~flfofq);
			Zynaddsubfx.eventParamToMsg(~chan, \flfoamp, ~flfoamp);


			~type = \midi;
			currentEnvironment.play;
		});
	}

	*initDict {
		Zynaddsubfx.oscInterfaceDict = (
			volume:		(msg: "/part0/Volume", convert: {|x| x.ampdb.max(-40.0).min(13.33)}),
			pan: 		(msg: "/part%/Ppanning", convert: {|x| x.linlin(-1.0,1.0, 0,127).asInteger}),
			modwheel: 	(msg: "/part%/ctl/modwheel.depth", convert: {|x| x.asInteger}),
			cutoff: 	(msg: "/part%/kit%/adpars/GlobalPar/GlobalFilter/basefreq", convert: {|x| x * 1.0}),
			res: 		(msg: "/part%/kit%/adpars/GlobalPar/GlobalFilter/baseq", convert: {|x| x.linlin(0.0,1.0,0.1,1000.0)}),
			fatk: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/A_dt", convert: {|x| x}),
			fatkl: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/PA_val", convert: {|x| x.asInteger}),
			fdec: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/D_dt", convert: {|x| x}),
			fdecl: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/PD_val", convert: {|x| x.asInteger}),
			frel: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/R_dt", convert: {|x| x}),
			frell: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/PR_val", convert: {|x| x.asInteger}),
			flfofq: 	(msg: "/part%/kit%/adpars/GlobalPar/FilterLfo/freq", convert: {|x| x.max(0.08).min(85.0) * 1.0}),
			flfoamp: 	(msg: "/part%/kit%/adpars/GlobalPar/FilterLfo/Pintensity", convert: {|x| x.asInteger}),
		);
		// Zynaddsubfx.oscInterfaceDict.keysValuesDo{|k,v| v.debug(k)};
	}

	*eventParamToMsg { |part=0, eventParam, value|
		if( value.isNil.not ) {
			Zynaddsubfx.send(
				addr: this.oscInterfaceDict[eventParam.asSymbol][\msg].replace($%, part),
				value: this.oscInterfaceDict[eventParam.asSymbol][\convert].(value.debug("zyn %".format(eventParam)))
			);
		}
	}

	*send { |addr, value|
		Zynaddsubfx.osc.sendMsg(addr, value);
	}

	*panic { Zynaddsubfx.osc.sendMsg("/Panic") }
}