Zynaddsubfx {
	classvar <>osc;

	*initClass {
		Zynaddsubfx.osc = NetAddr("localhost", 4001);

		Event.addEventType(\zynaddsubfx, {
			Zynaddsubfx.pan(~chan, ~pan.linlin(-1.0,1.0, 0,127));
			Zynaddsubfx.modwheel(~chan, ~modwheel);
			Zynaddsubfx.cutoff(~chan, ~cutoff);
			~type = \midi;
			currentEnvironment.play;
		});
	}

	*send { |addr, value|
		Zynaddsubfx.osc.sendMsg(addr, value);
	}

	*pan { |part, value|
		// if( value.isNil ) { value = 64 };
		if( value.isNil.not ) {
			// value.debug("pan");
			Zynaddsubfx.osc.sendMsg("/part%/Ppanning".format(part), value.asInteger);
		}
	}

	*modwheel { |part, value|
		if( value.isNil.not ) {
			// value.debug("modwheel");
			Zynaddsubfx.osc.sendMsg("/part%/ctl/modwheel.depth".format(part), value.asInteger)
		}
	}

	*cutoff { |part, freq, kit=0|
		if( freq.isNil.not ) {
			freq.debug("cutoff % %".format(part, kit));
			Zynaddsubfx.osc.sendMsg("/part%/kit%/adpars/GlobalPar/GlobalFilter/basefreq".format(part,kit), freq * 1.0); // convert to float
		}
	}
}