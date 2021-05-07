module Lab_4c(CLOCK_50, LEDR, SW, KEY);
    input CLOCK_50;
    input [17:0] SW, KEY;
    output [1:0] LEDR;
    wire toreg;
    wire[11:0] value;

    fiftymhz half(.clk(CLOCK_50), .s(2'b10), .out(toreg));
    morse mc(.SW(SW[2:0]), .out(value[11:0]));
    ebshft shfter(.SW(value), .sft(1'b1), .load(SW[17]), .clk(toreg), .rst(1'b1), .out(LEDR[0]));
endmodule

module morse(SW, out);
    input [2:0] SW;
    reg [11:0] temp;
    output [11:0] out;

    always @(*)
    begin
        case(SW[2:0])
            3'b000: temp <= 12'b101110000000;
            3'b001: temp <= 12'b111010101000;
            3'b010: temp <= 12'b111010111010;
            3'b011: temp <= 12'b111010100000;
            3'b100: temp <= 12'b100000000000;
            3'b101: temp <= 12'b101011101000;
            3'b110: temp <= 12'b111011101000;
            3'b111: temp <= 12'b101010100000;
        endcase
    end
    assign out = temp;
endmodule

module fiftymhz(clk, s, out);
    reg [26:0] count;
    reg [1:0] crochet;
    reg quaver;
    reg semiquaver;
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
            semiquaver <= 1'b0;
        end
        else begin
            semiquaver<= 1'b1;
        end

        if(crochet == 2'b00)
            crochetout <= 1'b1;
        else
            crochetout <= 1'b0;

        if(quaver == 1'b0)
            quaverout <= 1'b1;
        else
            quaverout <= 1'b0;

        if(semiquaver == 1'b0)
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

module ebshft(SW, sft, load, clk, rst, out);
    input [11:0] SW;
    input sft, load, rst, clk;
    output out;
    wire [11:0] sbw;
    shifterbit sb11(.loadval(SW[0]), .in(1'b0), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[11]));
    shifterbit sb10(.loadval(SW[1]), .in(sbw[11]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[10]));
    shifterbit sb9(.loadval(SW[2]), .in(sbw[10]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[9]));
    shifterbit sb8(.loadval(SW[3]), .in(sbw[9]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[8]));
    shifterbit sb7(.loadval(SW[4]), .in(sbw[8]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[7]));
    shifterbit sb6(.loadval(SW[5]), .in(sbw[7]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[6]));
    shifterbit sb5(.loadval(SW[6]), .in(sbw[6]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[5]));
    shifterbit sb4(.loadval(SW[7]), .in(sbw[5]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[4]));
    shifterbit sb3(.loadval(SW[8]), .in(sbw[4]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[3]));
    shifterbit sb2(.loadval(SW[9]), .in(sbw[3]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[2]));
    shifterbit sb1(.loadval(SW[10]), .in(sbw[2]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(sbw[1]));
    shifterbit sb0(.loadval(SW[11]), .in(sbw[1]), .shift(sft), .load_n(load), .clk(clk), .reset(rst), .out(out));

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
