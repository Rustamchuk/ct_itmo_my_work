module control_unit(opcode, funct, memto_reg, mem_write, branch, alu_control, alu_src, reg_dst, reg_write);
  input [5:0] opcode;
  input [5:0] funct;

  wire [5:0] lw = 6'b100011, sw = 6'b101011, beq = 6'b000100, r_type = 6'b000000;

  output [2:0] alu_control;
  output memto_reg, mem_write, branch, alu_src, reg_dst, reg_write;

  reg [2:0] reg_alu_control;
  reg [1:0] alu_op;
  reg reg_memto_reg, reg_mem_write, reg_branch, reg_alu_src, reg_reg_dst, reg_reg_write;
  
  always @(*) begin
    case(opcode)
      lw : begin
        reg_memto_reg = 1'b1;
        reg_mem_write = 1'b0;
        reg_branch = 1'b0;
        alu_op = 2'b00;
        reg_alu_src = 1'b1;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b1;
      end
      sw : begin
        reg_memto_reg = 1'b1;
        reg_mem_write = 1'b1;
        reg_branch = 1'b0;
        alu_op = 2'b00;
        reg_alu_src = 1'b1;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b0;
      end
      beq : begin
        reg_memto_reg = 1'b1;
        reg_mem_write = 1'b0;
        reg_branch = 1'b1;
        alu_op = 2'b01;
        reg_alu_src = 1'b0;
        reg_reg_dst = 1'b0;
        reg_reg_write = 1'b0;
      end
      r_type : begin
        reg_memto_reg = 1'b0;
        reg_mem_write = 1'b0;
        reg_branch = 1'b0;
        alu_op = 2'b10;
        reg_alu_src = 1'b0;
        reg_reg_dst = 1'b1;
        reg_reg_write = 1'b1;
      end
    endcase
  end

  assign memto_reg = reg_memto_reg;
  assign mem_write = reg_mem_write;
  assign branch = reg_branch;
  assign alu_src = reg_alu_src;
  assign reg_dst = reg_reg_dst;
  assign reg_write = reg_reg_write;

  alu_decoder decoder (alu_op, funct, alu_control);
endmodule

module alu_decoder (alu_op, funct, alu_control);
  input [1:0] alu_op;
  input [5:0] funct;

  wire [1:0] add = 2'b00, subtract = 2'b01, look_funct = 2'b10;
  wire [5:0] add_fun = 6'b100000, sub_fun = 6'b100010, and_fun = 6'b100100, or_fun = 6'b100101, slt_fun = 6'b101010;

  output [2:0] alu_control;

  reg [2:0] reg_alu_control;
  reg start_decode = 1'b0;

  always @(*) begin
    case(alu_op)
      add : 
        reg_alu_control = 3'b010;
      subtract : 
        reg_alu_control = 3'b110;
      look_funct : 
        case(funct)
          add_fun:
            reg_alu_control = 3'b010;
          sub_fun:
            reg_alu_control = 3'b110;
          and_fun:
            reg_alu_control = 3'b000;
          or_fun:
            reg_alu_control = 3'b001;
          slt_fun:
            reg_alu_control = 3'b111;
        endcase
    endcase
  end

  // funct_decoder decoder_fff (funct, alu_control);
  assign alu_control = reg_alu_control;
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