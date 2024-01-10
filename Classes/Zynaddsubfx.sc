// A class to control Zynaddsubfx from SC via OSC.
// This is not exhaustive (yet). Just exposing some useful parameters to be live coded
//
// GUIDE
// -----
//
// To expose more controls, add them to the oscInterfaceDict
//

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
			// part settings
			Zynaddsubfx.eventParamToMsg(~chan, \volume, ~amp);
			Zynaddsubfx.eventParamToMsg(~chan, \pan, ~pan);
			// ctl
			Zynaddsubfx.eventParamToMsg(~chan, \modwheel, ~modwheel);

			// add synth - global
			// amp env
			Zynaddsubfx.eventParamToMsg(~chan, \atk, ~atk);
			Zynaddsubfx.eventParamToMsg(~chan, \dec, ~dec);
			Zynaddsubfx.eventParamToMsg(~chan, \sus, ~rel);
			Zynaddsubfx.eventParamToMsg(~chan, \rel, ~rel);
			// amp lfo
			Zynaddsubfx.eventParamToMsg(~chan, \lfofq, ~lfofq);
			Zynaddsubfx.eventParamToMsg(~chan, \lfoamp, ~lfoamp);
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
			// part
			volume:		(msg: "/part0/Volume", convert: {|x| x.ampdb.max(-40.0).min(13.33)}),
			pan: 		(msg: "/part%/Ppanning", convert: {|x| x.linlin(-1.0,1.0, 0,127).asInteger}),
			// ctl
			modwheel: 	(msg: "/part%/ctl/modwheel.depth", convert: {|x| x.asInteger}),
			// add synth - global
			// amp env
			atk:		(msg: "/part%/kit%/adpars/GlobalPar/AmpEnvelope/A_dt", convert: {|x| x.max(0).min(41.0) * 1.0}),
			dec:		(msg: "/part%/kit%/adpars/GlobalPar/AmpEnvelope/D_dt", convert: {|x| x.max(0).min(41.0) * 1.0}),
			sus:		(msg: "/part%/kit%/adpars/GlobalPar/AmpEnvelope/PS_val", convert: {|x| x.linlin(0.0,1.0, 0,127).asInteger}),
			rel:		(msg: "/part%/kit%/adpars/GlobalPar/AmpEnvelope/R_dt", convert: {|x| x.max(0).min(41.0) * 1.0}),
			// amp lfo
			lfofq: 		(msg: "/part%/kit%/adpars/GlobalPar/AmpLfo/freq", convert: {|x| x.max(0.08).min(85.0) * 1.0}),
			lfoamp: 	(msg: "/part%/kit%/adpars/GlobalPar/AmpLfo/Pintensity", convert: {|x| x.asInteger}),
			// filter
			cutoff: 	(msg: "/part%/kit%/adpars/GlobalPar/GlobalFilter/basefreq", convert: {|x| x * 1.0}),
			res: 		(msg: "/part%/kit%/adpars/GlobalPar/GlobalFilter/baseq", convert: {|x| x.linlin(0.0,1.0,0.1,1000.0)}),
			// filter env
			fatk: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/A_dt", convert: {|x| x}),
			fatkl: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/PA_val", convert: {|x| x.asInteger}),
			fdec: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/D_dt", convert: {|x| x}),
			fdecl: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/PD_val", convert: {|x| x.asInteger}),
			frel: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/R_dt", convert: {|x| x}),
			frell: 		(msg: "/part%/kit%/adpars/GlobalPar/FilterEnvelope/PR_val", convert: {|x| x.asInteger}),
			// filter lfo
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