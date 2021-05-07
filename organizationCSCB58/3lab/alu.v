`timescale 1ns / 1ns // `timescale time_unit/time_precision
module Lab02_c(SW, KEY, HEX0, HEX1, HEX2, HEX3, HEX4, HEX5, LEDR);
    input [8:0] SW;
    input [2:0] KEY;
    output [6:0] HEX0, HEX1, HEX2, HEX3, HEX4, HEX5;
    output [9:0] LEDR;

    ALU alu(.SW(SW[8:0]), .KEY(KEY[2:0]), .HEX0(HEX0[6:0]), .HEX1(HEX1[6:0]), .HEX2(HEX2[6:0]), .HEX3(HEX3[6:0]), .HEX4(HEX4[6:0]), .HEX5(HEX5[6:0]), .LEDR(LEDR[9:0]));

endmodule

module fulladder(a, b, c, out, cout);
    input a, b, c;
    output cout, out;
    assign cout = a & b | a & c | b & c;
    assign out = a & b & c | a & ~b & ~c | ~a & b & ~c | ~a & ~b & c;
endmodule

module fa4bit(in, cin, cout, out);
    input [7:0] in;
    input cin;
    output [3:0] out;
    output cout;
    wire [3:0] c;

    fulladder fa0(
        .a(in[4]),
        .b(in[0]),
        .c(cin),
        .cout(c[0]),
        .out(out[0]));

    fulladder fa1(
        .a(in[5]),
        .b(in[1]),
        .c(c[0]),
        .cout(c[1]),
        .out(out[1]));

    fulladder fa2(
        .a(in[6]),
        .b(in[2]),
        .c(c[1]),
        .cout(c[2]),
        .out(out[2]));
        
    fulladder fa3(
        .a(in[7]),
        .b(in[3]),
        .c(c[2]),
        .cout(cout),
        .out(out[3]));
endmodule

module ALU(SW, KEY, HEX0, HEX1, HEX2, HEX3, HEX4, HEX5, LEDR);
    input [8:0] SW;
    input [2:0] KEY;
    output [6:0] HEX0, HEX1, HEX2, HEX3, HEX4, HEX5;
    output [7:0] LEDR;
    reg [7:0] temp;//, veradd;
    wire[7:0] xrr, bwo, bwa, ito, veradd;
    wire[4:0] add;

    // display to input hex
    //assign HEX0 = SW[3:0];
    hex_display hex0(.IN(SW[3:0]), .OUT(HEX0[6:0]));
    //assign HEX2 = SW[7:4];
    hex_display hex2(.IN(SW[7:4]), .OUT(HEX2[6:0]));
    // 0 at hex 1 and 3
    //assign HEX1 = 4'b0000;
    hex_display hex1(.IN(4'b0000), .OUT(HEX1[6:0]));
    //assign HEX3 = 4'b0000;
    hex_display hex3(.IN(4'b0000), .OUT(HEX3[6:0]));

    fa4bit adding(.in(SW[7:0]), .cin(1'b0), .cout (add[4]), .out(add[3:0]));// fa4bit
    assign veradd[4:0] = SW[7:4] + SW[3:0];// +
    XOROR xoror(.a(SW[7:4]), .b(SW[3:0]), .out(xrr[7:0]));// XOROR
    bwOR bwor(.a(SW[7:4]), .b(SW[3:0]), .out(bwo[7:0]));// bwOR
    bwAND bwand(.a(SW[7:4]), .b(SW[3:0]), .out(bwa[7:0]));// bwAND
    intoout io(.a(SW[7:4]), .b(SW[3:0]), .out(ito[7:0]));// intoout

    // desired function
    always @(*)
    begin:generating
        case(KEY[2:0]) // keys are 1 when not pressed
            3'b000:
            begin
                temp[7:5] = 3'b000;
                temp[4:0] = add;
            end // fa4bit
            3'b001:
            begin
                temp[7:4] = 3'b000;
                temp[4:0] = veradd; 
             end//+
            3'b010: temp = xrr;//XOROR
            3'b011: temp = bwo;//bwOR
            3'b100: temp = bwa;//bwAND
            3'b101: temp = ito;//intoout
            default: temp = 8'b00000000; // output 0
        endcase
    end

    assign LEDR = temp;
    //assign HEX4 = temp[3:0];
    hex_display hex4(.IN(temp[3:0]), .OUT(HEX4[6:0]));
    //assign HEX5 = temp[7:4];
    hex_display hex5(.IN(temp[7:4]), .OUT(HEX5[6:0]));
endmodule

module bwOR(a, b, out);
    input [3:0] a, b;
    output reg [7:0] out;

    always @(*)
    begin
    if(a | b)
        out = 8'b00000001;
    else
        out = 8'b00000000;
    end
endmodule
    
module bwAND(a, b, out);
    input [3:0] a, b;
    output reg [7:0] out;

    always @(*)
    begin
    if(& {a, b})
        out = 8'b00000001;
    else
        out = 8'b00000000;
    end
endmodule

module XOROR(a, b, out);
    input [3:0] a, b;
    output [7:0] out;

    assign out[7:4] = (a | b);
    assign out[3:0] = (a ^ b);
endmodule

module intoout(a, b, out);
    input [3:0] a, b;
    output [7:0] out;

    assign out[7:4] = b;
    assign out[3:0] = a;
endmodule

module hex_display(IN, OUT);
    input [3:0] IN;
     output reg [7:0] OUT;
     
     always @(*)
     begin
        case(IN[3:0])
            4'b0000: OUT = 7'b1000000;
            4'b0001: OUT = 7'b1111001;
            4'b0010: OUT = 7'b0100100;
            4'b0011: OUT = 7'b0110000;
            4'b0100: OUT = 7'b0011001;
            4'b0101: OUT = 7'b0010010;
            4'b0110: OUT = 7'b0000010;
            4'b0111: OUT = 7'b1111000;
            4'b1000: OUT = 7'b0000000;
            4'b1001: OUT = 7'b0011000;
            4'b1010: OUT = 7'b0001000;
            4'b1011: OUT = 7'b0000011;
            4'b1100: OUT = 7'b1000110;
            4'b1101: OUT = 7'b0100001;
            4'b1110: OUT = 7'b0000110;
            4'b1111: OUT = 7'b0001110;
            
            default: OUT = 7'b0111111;
        endcase

    end
endmodule
