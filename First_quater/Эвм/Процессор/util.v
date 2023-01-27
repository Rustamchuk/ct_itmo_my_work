// модуль, который реализует расширенение
// 16-битной знаковой константы до 32-битной
module sign_extend(in, out);
  input [15:0] in;
  output [31:0] out;

  assign out = {{16{in[15]}}, in};
endmodule

// модуль, который реализует побитовый сдвиг числа
// влево на 2 бита
module shl_2(in, out);
  input [31:0] in;
  output [31:0] out;

  assign out = {in[29:0], 2'b00};
endmodule

// 32 битный сумматор
module adder(a, b, out);
  input [31:0] a, b;
  output [31:0] out;

  assign out = a + b;
endmodule

// 32-битный мультиплексор
module mux2_32(d0, d1, a, out);
  input [31:0] d0, d1;
  input a;
  output [31:0] out;
  assign out = a ? d1 : d0;
endmodule

// 5 - битный мультиплексор
module mux2_5(d0, d1, a, out);
  input [4:0] d0, d1;
  input a;
  output [4:0] out;
  assign out = a ? d1 : d0;
endmodule

module alu(a, b, control, res, zero);
  input [31:0] a, b;
  input [2:0] control;
  output [31:0] res;
  output zero;

  wire [2:0] add = 3'b010, sub = 3'b110, kon = 3'b000, dis = 3'b001, slt = 3'b111;

  reg [31:0] reg_res;

  always @(*) begin
    case(control)
      add : 
        reg_res = a + b;
      sub : 
        reg_res = a - b;
      kon : 
        reg_res = a & b;
      dis :
        reg_res = a | b;
      slt : begin
        // if (a < b) begin
        //   reg_res = 32'b00000000000000000000000000000001;
        // end else begin
        //   reg_res = 32'b00000000000000000000000000000000;
        // end
        reg_res[31:1] = 31'b0000000000000000000000000000000;
        reg_res[0] = a[31] != b[31] ? (a[31] > b[31] ? 1 : 0) : (a < b ? 1 : 0);
        // if (a[31] == b[31]) begin
        //   if (a[31] == 1'b1) begin
            
        //   end
        // end
      end
    endcase
  end
  
  assign zero = reg_res == 32'b00000000000000000000000000000000;

  assign res = reg_res;

endmodule

module control_unit(opcode, funct, memto_reg, mem_write, bne_out, beq_out, alu_control, alu_src, reg_dst, reg_write, j_out, jal_out, jr);
  input [5:0] opcode;
  input [5:0] funct;

  wire [5:0] lw = 6'b100011, sw = 6'b101011, beq = 6'b000100, r_type = 6'b000000, addi = 6'b001000, andi = 6'b001100, bne = 6'b000101, j = 6'b000010, jal = 6'b000011;

  output [2:0] alu_control;
  output memto_reg, mem_write, bne_out, beq_out, alu_src, reg_dst, reg_write, j_out, jal_out, jr;

  reg [2:0] reg_alu_control;
  reg [1:0] alu_op;
  reg reg_memto_reg, reg_mem_write, reg_bne, reg_beq, reg_alu_src, reg_reg_dst, reg_reg_write, reg_j, reg_jal;
  
  always @(*) begin
    case(opcode)
      lw : begin
        reg_memto_reg = 1'b1;
        reg_mem_write = 1'b0;
        reg_bne = 1'b0;
        reg_beq = 1'b0;
        alu_op = 2'b00;
        reg_alu_src = 1'b1;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b1;
        reg_j = 1'b0;
        reg_jal = 1'b0;
      end
      sw : begin
        reg_memto_reg = 1'b0;
        reg_mem_write = 1'b1;
        reg_bne = 1'b0;
        reg_beq = 1'b0;
        alu_op = 2'b00;
        reg_alu_src = 1'b1;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b0;
        reg_j = 1'b0;
        reg_jal = 1'b0;
      end
      beq : begin
        reg_memto_reg = 1'b0;
        reg_mem_write = 1'b0;
        reg_bne = 1'b0;
        reg_beq = 1'b1;
        alu_op = 2'b01;
        reg_alu_src = 1'b0;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b0;
        reg_j = 1'b0;
        reg_jal = 1'b0;
      end
      r_type : begin
        reg_memto_reg = 1'b0;
        reg_mem_write = 1'b0;
        reg_bne = 1'b0;
        reg_beq = 1'b0;
        alu_op = 2'b10;
        reg_alu_src = 1'b0;
        reg_reg_dst = 1'b1;
        if (funct == 6'b001000)
          reg_reg_write = 1'b0;
        else
          reg_reg_write = 1'b1;
        reg_j = 1'b0;
        reg_jal = 1'b0;
      end
      addi: begin
        reg_memto_reg = 1'b0;
        reg_mem_write = 1'b0;
        reg_bne = 1'b0;
        reg_beq = 1'b0;
        alu_op = 2'b00;
        reg_alu_src = 1'b1;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b1;
        reg_j = 1'b0;
        reg_jal = 1'b0;
      end
      andi: begin
        reg_memto_reg = 1'b0;
        reg_mem_write = 1'b0;
        reg_bne = 1'b0;
        reg_beq = 1'b0;
        alu_op = 2'b11;
        reg_alu_src = 1'b1;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b1;
        reg_j = 1'b0;
        reg_jal = 1'b0;
      end
      bne: begin
        reg_memto_reg = 1'b0;
        reg_mem_write = 1'b0;
        reg_bne = 1'b1;
        reg_beq = 1'b0;
        alu_op = 2'b01;
        reg_alu_src = 1'b0;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b0;
        reg_j = 1'b0;
        reg_jal = 1'b0;
      end
      j: begin
        reg_memto_reg = 1'b0;
        reg_mem_write = 1'b0;
        reg_bne = 1'b0;
        reg_beq = 1'b0;
        alu_op = 2'b00;
        reg_alu_src = 1'b0;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b0;
        reg_jal = 1'b0;
        reg_j = 1'b1;
      end
      jal: begin
        reg_memto_reg = 1'b0;
        reg_mem_write = 1'b0;
        reg_bne = 1'b0;
        reg_beq = 1'b0;
        alu_op = 2'b00;
        reg_alu_src = 1'b0;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b1;
        reg_j = 1'b1;
        reg_jal = 1'b1;
      end
    endcase
  end

  assign memto_reg = reg_memto_reg;
  assign mem_write = reg_mem_write;
  assign bne_out = reg_bne;
  assign beq_out = reg_beq;
  assign alu_src = reg_alu_src;
  assign reg_dst = reg_reg_dst;
  assign reg_write = reg_reg_write;
  assign j_out = reg_j;
  assign jal_out = reg_jal;

  alu_decoder decoder (alu_op, funct, alu_control, jr);
endmodule

module alu_decoder (alu_op, funct, alu_control, jr);
  input [1:0] alu_op;
  input [5:0] funct;

  wire [1:0] add = 2'b00, subtract = 2'b01, look_funct = 2'b10, andi = 2'b11;
  wire [5:0] add_fun = 6'b100000, sub_fun = 6'b100010, and_fun = 6'b100100, or_fun = 6'b100101, slt_fun = 6'b101010, jr_fun = 6'b001000;

  output [2:0] alu_control;
  output jr;

  reg [2:0] reg_alu_control;
  reg reg_jr;

  always @(*) begin
    case(alu_op)
      add : begin 
        reg_alu_control = 3'b010;
        reg_jr = 1'b0;
      end
      subtract : begin 
        reg_alu_control = 3'b110;
        reg_jr = 1'b0;
      end
      look_funct : 
        case(funct)
          add_fun: begin
            reg_alu_control = 3'b010;
            reg_jr = 1'b0;
          end
          sub_fun: begin 
            reg_alu_control = 3'b110;
            reg_jr = 1'b0;
          end
          and_fun: begin
            reg_alu_control = 3'b000;
            reg_jr = 1'b0;
          end
          or_fun: begin
            reg_alu_control = 3'b001;
            reg_jr = 1'b0;
          end
          slt_fun: begin
            reg_alu_control = 3'b111;
            reg_jr = 1'b0;
          end
          jr_fun: begin
            reg_alu_control = 3'b100;
            reg_jr = 1'b1;
          end
        endcase
      andi : begin
        reg_alu_control = 3'b000;
        reg_jr = 1'b0;
      end
    endcase
  end

  // funct_decoder decoder_fff (funct, alu_control);
  assign alu_control = reg_alu_control;
  assign jr = reg_jr;
endmodule

module my_not(a, out);
  input a;
  output out;
  supply1 sup;
  supply0 gnd;

  pmos p1 (out, sup, a);
  nmos n1 (out, gnd, a);
endmodule

// module funct_decoder (funct, alu_control);
//   input [5:0] funct;

//   wire [5:0] add = 6'b100000, subtract = 6'b100010, andd = 6'b100100, orr = 6'b100101, slt = 6'b101010;

//   output [2:0] alu_control;

//   reg [2:0] reg_alu_control;

//   always @(*) begin
//     case(funct)
//       add :
//         reg_alu_control = 3'b010;
//       subtract :
//         reg_alu_control = 3'b110;
//       andd : 
//         reg_alu_control = 3'b000;
//       orr : 
//         reg_alu_control = 3'b001;
//       slt : 
//         reg_alu_control = 3'b111;
//     endcase
//   end

//   assign alu_control = reg_alu_control;
// endmodule