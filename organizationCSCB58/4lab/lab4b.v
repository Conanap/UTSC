module Lab_4b(SW, HEX0, CLOCK_50);
    input [2:0] SW;
    output [6:0] HEX0;
    input CLOCK_50;
    wire clk;
    wire[7:0] boop;

    fiftymhz hzcount(.clk(CLOCK_50), .s(SW[1:0]), .out(clk));
    something turnsoutacounter(.en(1'b1), .clk(clk), .clr(SW[2]), .out(boop));
    hex_display h1(.IN(boop[3:0]), .OUT(HEX0));
endmodule

module something(en, clk, clr, out);
    input en, clk, clr;
    output[7:0] out;

    wire f0t1, f1t2, f2t3, f3t4, f4t5, f5t6, f6t7;
    wire [6:0] anded;
    assign anded[0] = en & f0t1;
    assign anded[1] = anded[0] & f1t2;
    assign anded[2] = anded[1] & f2t3;
    assign anded[3] = anded[2] & f3t4;
    assign anded[4] = anded[3] & f4t5;
    assign anded[5] = anded[4] & f5t6;
    assign anded[6] = anded[5] & f6t7;

    ff f0(en, f0t1, clk, clr);
    ff f1(anded[0], f1t2, clk, clr);
    ff f2(anded[1], f2t3, clk, clr);
    ff f3(anded[2], f3t4, clk, clr);
    ff f4(anded[3], f4t5, clk, clr);
    ff f5(anded[4], f5t6, clk, clr);
    ff f6(anded[5], f6t7, clk, clr);
    ff f7(anded[6], out[7], clk, clr);
    assign out[0] = f0t1;
    assign out[1] = f1t2;
    assign out[2] = f2t3;
    assign out[3] = f3t4;
    assign out[4] = f4t5;
    assign out[5] = f5t6;
    assign out[6] = f6t7;
endmodule

module ff(in, out, clock, reset);
    input in, clock, reset;
    reg temp = 1'b0;
    output out;

    always@(posedge clock, negedge reset)
    begin
        if(reset == 1'b0)
            temp <= 0;
        else if (in == 1'b1) begin
            if (temp == 1'b0)
                temp <= 1'b1;
            else
                temp <= 1'b0;
        end
    end

    assign out = temp;
endmodule

module fiftymhz(clk, s, out);
    reg [26:0] count;
    reg [1:0] crochet;
    reg [2:0] quaver;
    reg [3:0] semiquaver;
    input [1:0] s;
    input clk;
    output out;
    reg crochetout = 1'b0; 
    reg quaverout = 1'b0;
    reg semiquaverout = 1'b0;

    always@(posedge clk)
    begin
        count <= count + 1'b1;
        if(count == 27'd12499999)
            count <= 27'b000000000000000000000000000;
        if(count == 27'b000000000000000000000000000)
        begin
            crochet <= crochet + 1'b1;
            quaver <= quaver + 1'b1;
            semiquaver <= semiquaver + 1'b1;
        end

        if(crochet == 2'b00) begin
            crochetout <= 1'b1;
        end
        else
            crochetout <= 1'b0;

        if(quaver == 1'b0 && count == 27'd0) begin
            quaverout <= 1'b1;
        end
        else
            quaverout <= 1'b0;

        if(semiquaver == 1'b0 && count == 27'd0)
            semiquaverout <= 1'b1;
        else
            semiquaverout <= 1'b0;
    end

    mux4to1 m4t1(s, clk, crochetout, quaverout, semiquaverout, out);
endmodule

module mux4to1(sw, in1, in2, in3, in4, out);
    input [1:0] sw;
    input in1, in2, in3, in4;
    output out;
    reg temp;

    always @(*) begin
        case(sw[1:0])
            2'b00: temp <= in1;
            2'b01: temp <= in2;
            2'b10: temp <= in3;
            2'b11: temp <= in4;
        endcase
    end

    assign out = temp;
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