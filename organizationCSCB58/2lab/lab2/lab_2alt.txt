module mux7to1(in, select, out);
    input [6:0] in;
    input [2:0] select;
    output out;

    always @(*)
    begin
        case(select[2:0])
            3'b000: out = in[0];
            3'b001: out = in[1];
            3'b010: out = in[2];
            3'b011: out = in[3];
            3'b100: out = in[4];
            3'b101: out = in[5];
            3'b110: out = in[6];
            default: out = in[6];
        endcase
    end
endmodule

module fulladder(a, b, c, out, cout);
    input a, b, c;
    output cout, out;
    assign cout = a & b | a & c | b & c;
    assign out = a & b & c | a & ~b & ~c | ~a & b & ~c | ~a & ~b & c;
endmodule

module fa4bit(in, out);
    input [7:0] in;
    output [4:0] out;
    wire [3:0] c;

    fulladder fa0(
        .a(in[4]),
        .b(in[0]),
        .c(1'b0),
        .cout(c[0]),
        .out(out[0]));

    fulladder fa1(
        .a(in[5]),
        .b(in[1]),
        .c(c[0]),
        .cout(c[1]),
        .out(out[0]));

    fulladder fa2(
        .a(in[6]),
        .b(in[2]),
        .c(c[1]),
        .cout(c[2]),
        .out(out[0]));
        
    fulladder fa3(
        .a(in[7]),
        .b(in[3]),
        .c(c[2]),
        .cout(out[4]),
        .out(out[3]));
endmodule

module ALU(SW, KEY, HEX0, HEX1, HEX2, HEX3, HEX4, HEX5, LEDR);
    input [7:0] SW;
    input [2:0] KEY;
    output [6:0] HEX0, HEX1, HEX2, HEX3, HEX4, HEX5;
    output [7:0] LEDR;
    reg [7:0] temp;//, veradd;
    wire[7:0] xrr, bwo, bwa, ito, veradd;
    wire[4:0] add;

    // display to input hex
    assign HEX0 = SW[3:0];
    assign HEX2 = SW[7:4];
    // 0 at hex 1 and 3
    assign HEX1 = 4'b0000;
    assign HEX3 = 4'b0000;

    fa4bit adding(.in(SW[7:0]), .out(add[4:0]));// fa4bit
    assign veradd[4:0] = SW[7:4] + SW[3:0];// +
    XOROR xoror(.a(SW[7:4]), .b(SW[3:0]), .out(xrr[7:0]));// XOROR
    bwOR bwor(.a(SW[7:4]), .b(SW[3:0]), .out(bwo[7:0]));// bwOR
    bwAND bwand(.a(SW[7:4]), .b(SW[3:0]), .out(bwa[7:0]));// bwAND
    intoout io(.a(SW[7:4]), .b(SW[3:0]), .out(ito[97:0]));// intoout

    // desired function
    always @(*)
    begin:generating
        case(KEY[2:0]) // keys are 1 when not pressed
            3'b000:
            begin
                //temp[7:5] = 3'b000;
                //temp[4:0] = add;
                assign LEDR[7:5] = 3'b000;
                assign LEDR[4:0] = add;
                assign HEX4[3:1] = 3'b000;
                assign HEX4[0] = add[4];
                assign HEX5[3:0] = add[3:0];
            end // fa4bit
            3'b001:
            begin
                //temp[7:5] = 3'b000;
                //temp[4:0] = veradd;
                assign LEDR[7:5] = 3'b000;
                assign LEDR[4:0] = veradd;
                assign HEX4[3:1] = 3'b000;
                assign HEX4[0] = veradd[4];
                assign HEX5[3:0] = veradd[3:0];
             end//+
            3'b010:
            begin
                assign LEDR = xrr;
                assign HEX4 = xrr[3:0];
                assign HEX5 = xrr[7:4];
            end
            3'b011: 
            begin
                assign LEDR = bwo;
                assign HEX4 = bwo[3:0];
                assign HEX5 = bwo[7:4];
            end
            //temp = bwo;//bwOR
            3'b100: begin
                assign LEDR = bwa;
                assign HEX4 = bwa[3:0];
                assign HEX5 = bwa[7:4];
            end
            //temp = bwa;//bwAND
            3'b101: 
                begin
                    assign LEDR = ito;
                    assign HEX4 = ito[3:0];
                    assign HEX5 = ito[7:4];
                end
            //temp = ito;//intoout
            default: 
                begin
                    LEDR = 8'b00000000;
                    HEX4 = 4'b0000;
                    HEX5 = 4'b0000;
                end
            //temp = 8'b00000000; // output 0
        endcase
    end

    //assign LEDR = temp;
    //assign HEX4 = temp[3:0];
    //assign HEX5 = temp[7:4];
endmodule

module bwOR(a, b, out);
    input [3:0] a, b;
    output [7:0] out;

    always @(*)
    begin
    if(a | b)
        assign out = 8'b00000001;
    else
        assign out = 8'b00000000;
    end
endmodule

module bwAND(a, b, out);
    input [3:0] a, b;
    output [7:0] out;

    always @(*)
    begin
    if(a & b)
        assign out = 8'b00000001;
    else
        assign out = 8'b00000000;
    end
endmodule

module XOROR(a, b, out);
    input [3:0] a, b;
    output [7:0] out;

    assign out[7:4] = (a ^ b);
    assign out[3:0] = (a & b);
endmodule

module intoout(a, b, out);
    input [3:0] a, b;
    output [7:0] out;

    assign out[7:4] = a;
    assign out[3:0] = b;
endmodule

