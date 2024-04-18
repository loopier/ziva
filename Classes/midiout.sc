 + MIDIOut {
	 // valueLSB is optional
	 nrpn { | chan, paramMSB, paramLSB, valueMSB, valueLSB=0|
		 this.control(chan, 99, paramMSB);
		 this.control(chan, 98, paramLSB);
		 this.control(chan, 6, valueMSB);
		 this.control(chan, 38, valueLSB);
	 }
 }