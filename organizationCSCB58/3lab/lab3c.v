module lab3_b(LEDR, SW, KEY);
    input [9:0] SW;
    input [3:0] KEY;
    output [7:0] LEDR;
	 wire [7:0] w;

	oneBitShifter s0(.loadVal(SW[7]), .IN(KEY[3]), .OUT(w[7]), .shift(KEY[2]), .load_n(KEY[1]),.reset_n(SW[9]),.clock(KEY[0]) );
	oneBitShifter s1(.loadVal(SW[6]), .IN(w[7]), .OUT(w[6]), .shift(KEY[2]), .load_n(KEY[1]),.reset_n(SW[9]),.clock(KEY[0]) );	 
	oneBitShifter s2(.loadVal(SW[5]), .IN(w[6]), .OUT(w[5]), .shift(KEY[2]), .load_n(KEY[1]),.reset_n(SW[9]),.clock(KEY[0]) );	 
 	oneBitShifter s3(.loadVal(SW[4]), .IN(w[5]), .OUT(w[4]), .shift(KEY[2]), .load_n(KEY[1]),.reset_n(SW[9]),.clock(KEY[0]) );
	oneBitShifter s4(.loadVal(SW[3]), .IN(w[4]), .OUT(w[3]), .shift(KEY[2]), .load_n(KEY[1]),.reset_n(SW[9]),.clock(KEY[0]) );
	oneBitShifter s5(.loadVal(SW[2]), .IN(w[3]), .OUT(w[2]), .shift(KEY[2]), .load_n(KEY[1]),.reset_n(SW[9]),.clock(KEY[0]) );
 	oneBitShifter s6(.loadVal(SW[1]), .IN(w[2]), .OUT(w[1]), .shift(KEY[2]), .load_n(KEY[1]),.reset_n(SW[9]),.clock(KEY[0]) );  
	oneBitShifter s7(.loadVal(SW[0]), .IN(w[1]), .OUT(w[0]), .shift(KEY[2]), .load_n(KEY[1]),.reset_n(SW[9]),.clock(KEY[0]) );
	
	LEDdisplay led0(.IN(w[7:0]), .OUT(LEDR[7:0]));
endmodule

module flipflop(d,q,clock,reset_n);

	input  d;
	input clock, reset_n;
	output reg q;
    always @(posedge clock)
    begin
        if(reset_n == 1'b0)
            q<=0;
        else
            q<=d;
    end

endmodule

module mux2to1(x, y, s, m);
    input x; //selected when s is 0
    input y; //selected when s is 1
    input s; //select signal
    output m; //o	
	
  
    assign m = s & y | ~s & x;
    // OR
    // assign m = s ? y : x;

endmodule

module oneBitShifter(loadVal, IN, OUT, shift, load_n,reset_n,clock );

	input IN,loadVal,shift,load_n,reset_n,clock;
	output OUT;
	
	wire [1:0] w;
	
	mux2to1 m0(.x(OUT),
	.y(IN),
	.s(shift),
	.m(w[0])
	);
	
	mux2to1 m1(.x(loadVal),
	.y(w[0]),
	.s(load_n),
	.m(w[1])
	);
	
	
	flipflop f(
		.d(w[1]),
		.q(OUT),
		.clock(clock),
		.reset_n(reset_n)
		);

endmodule

		
module LEDdisplay(IN,OUT);

	input[7:0] IN;
	output [7:0] OUT ;
	assign OUT = IN;

	
endmodule