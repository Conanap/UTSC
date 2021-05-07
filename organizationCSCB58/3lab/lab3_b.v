module lab3_b(SW, KEY, LEDR);
    input [17:0] SW;
    input [3:0] KEY;
    output [7:0] LEDR;
    wire [7:0] sbw;
    wire adout;
    //ASR = key[3]

    asrdecider ad(.in(sbw[7]), .out(adout), .asr(SW[17]));

    shifterbit sb7(.loadval(SW[7]), .in(adout), .shift(SW[16]), .load_n(SW[15]), .clk(SW[8]), .reset(SW[9]), .out(sbw[7]));
    shifterbit sb6(.loadval(SW[6]), .in(sbw[7]), .shift(SW[16]), .load_n(SW[15]), .clk(SW[8]), .reset(SW[9]), .out(sbw[6]));
    shifterbit sb5(.loadval(SW[5]), .in(sbw[6]), .shift(SW[16]), .load_n(SW[15]), .clk(SW[8]), .reset(SW[9]), .out(sbw[5]));
    shifterbit sb4(.loadval(SW[4]), .in(sbw[5]), .shift(SW[16]), .load_n(SW[15]), .clk(SW[8]), .reset(SW[9]), .out(sbw[4]));
    shifterbit sb3(.loadval(SW[3]), .in(sbw[4]), .shift(SW[16]), .load_n(SW[15]), .clk(SW[8]), .reset(SW[9]), .out(sbw[3]));
    shifterbit sb2(.loadval(SW[2]), .in(sbw[3]), .shift(SW[16]), .load_n(SW[15]), .clk(SW[8]), .reset(SW[9]), .out(sbw[2]));
    shifterbit sb1(.loadval(SW[1]), .in(sbw[2]), .shift(SW[16]), .load_n(SW[15]), .clk(SW[8]), .reset(SW[9]), .out(sbw[1]));
    shifterbit sb0(.loadval(SW[0]), .in(sbw[1]), .shift(SW[16]), .load_n(SW[15]), .clk(SW[8]), .reset(SW[9]), .out(sbw[0]));

    assign LEDR = sbw;

endmodule

module asrdecider(in, out, asr);
    input in, asr;
    reg temp;
    output out;
    always@(*)
    begin:rar
        case(asr)
            1'b0: temp <= 0;
            1'b1: temp <= in;
        endcase
    end

    assign out = temp;
endmodule

module shifterbit(loadval, in, shift, load_n, clk, reset, out);
    input loadval, in, shift, load_n, clk, reset;
    output out;
    wire outwire, outwire2, outwire3;

    mux2to1 m1(.x(outwire3), .y(in), .s(shift), .m(outwire));
    mux2to1 m2(.x(loadval), .y(outwire), .s(load_n), .m(outwire2));
    ff f(.in(outwire2), .out(outwire3), .clock(clk), .reset(reset));
    assign out = outwire3;
endmodule

module mux2to1(x, y, s, m);
    input x, y, s;
    output m;

    assign m = s & y | ~s & x;
endmodule

module ff(in, out, clock, reset);
    input in, clock, reset;
    reg temp;
    output out;

    always@(posedge clock)
    begin
        if(reset == 1'b0)
            temp <= 0;
        else
            temp <= in;
    end

    assign out = temp;
endmodule