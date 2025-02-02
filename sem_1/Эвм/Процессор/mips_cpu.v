`include "util.v"

module mips_cpu(clk, pc, pc_new, instruction_memory_a, instruction_memory_rd, data_memory_a, data_memory_rd, data_memory_we, data_memory_wd,
                register_a1, register_a2, register_a3, register_we3, register_wd3, register_rd1, register_rd2);
  // сигнал синхронизации
  input clk;
  // текущее значение регистра PC
  inout [31:0] pc;
  // новое значение регистра PC (адрес следующей команды)
  output [31:0] pc_new;
  // we для памяти данных
  output data_memory_we;
  // адреса памяти и данные для записи памяти данных
  output [31:0] instruction_memory_a, data_memory_a, data_memory_wd;
  // данные, полученные в результате чтения из памяти
  inout [31:0] instruction_memory_rd, data_memory_rd;
  // we3 для регистрового файла
  output register_we3;
  // номера регистров
  output [4:0] register_a1, register_a2, register_a3;
  // данные для записи в регистровый файл
  output [31:0] register_wd3;
  // данные, полученные в результате чтения из регистрового файла
  inout [31:0] register_rd1, register_rd2;

  wire [2:0] alu_control;
  wire memto_reg, mem_write, bne, beq, alu_src, reg_dst, j, jal, jr;

  wire [31:0] signlmm, srcB, ALU_result, jump, PC_jump, PC_jal;
  wire zero, branch, bne_and, beq_and, not_zero;

  wire [31:0] moved_sign_lmm, pc_plus4, pc_branch;
  wire [31:0] pc_upgr, pc_for;
  wire [31:0] cur_wd3;
  wire [4:0] cur_a3;
  wire [4:0] ra = 5'b11111;

  // initial begin
  //   // Системная функция $display выводит текст на экран во время симуляции.
  //   $monitor("%b", instruction_memory_rd);
  // end

  control_unit ctrl_unit (instruction_memory_rd[31:26], instruction_memory_rd[5:0], 
  memto_reg, mem_write, bne, beq, alu_control, alu_src, reg_dst, register_we3, j, jal, jr);

  // initial begin
  //   // Системная функция $display выводит текст на экран во время симуляции.
  //   $monitor("%b", memto_reg);
  //   $monitor("%b", data_memory_we);
  //   $monitor("%b", branch);
  //   $monitor("%b", alu_src);
  //   $monitor("%b", reg_dst);
  //   $monitor("%b", register_we3);
  //   $monitor("%b", j);
  //   $monitor("%b", jal);
  // end
  
  sign_extend sixteen_to_thirtytwo (instruction_memory_rd[15:0], signlmm);

  // adder jal_add (pc, 32'b00000000000000000000000000000100, PC_jal);

  // mux2_32 jal_mux (pc, PC_jal, jal, instruction_memory_a);
  // assign instruction_memory_a = pc;

  assign data_memory_we = mem_write;

  assign register_a1 = instruction_memory_rd[25:21];
  assign register_a2 = instruction_memory_rd[20:16];

  mux2_5 choose_a3 (instruction_memory_rd[20:16], instruction_memory_rd[15:11], reg_dst, cur_a3);
  mux2_5 choose (cur_a3, ra, jal, register_a3);

  // register_file register_f (clk, reg_write, instruction_memory_rd[25:21], instruction_memory_rd[20:16], register_a3, data_memory_wd, register_rd1, register_rd2);

  mux2_32 choose_number_2 (register_rd2, signlmm, alu_src, srcB);

  alu my_alu (register_rd1, srcB, alu_control, ALU_result, zero);

  assign data_memory_wd = register_rd2;

  assign data_memory_a = ALU_result;

  adder pc_4 (pc, 32'b00000000000000000000000000000100, pc_plus4);

  mux2_32 fin_mux (ALU_result, data_memory_rd, memto_reg, cur_wd3);
  mux2_32 fin (cur_wd3, pc_plus4, jal, register_wd3);

  // assign moved_sign_lmm = signlmm << 2;

  shl_2 moved (signlmm, moved_sign_lmm);

  // assign jump = instruction_memory_rd[25:0] << 2;

  assign jump = instruction_memory_rd[25:0] << 2;
  
  // assign pc_plus4 = pc + 4;

  // always @(*) begin
  //   if (jal) begin
  //     instruction_memory_a = pc_plus4;
  //   end else begin
  //     instruction_memory_a = pc;
  //   end
  // end
  assign instruction_memory_a = pc;
  // assign out = jal ? pc_plus4 : pc;

  assign PC_jump[31:28] = pc_plus4[31:28];
  assign PC_jump[27:0] = jump[27:0];

  adder plus_sign_pc (moved_sign_lmm, pc_plus4, pc_branch);

  // assign and_zero_branch = zero & branch;
  
  assign beq_and = zero & beq;
  my_not not_zer(zero, not_zero);
  assign bne_and = not_zero & bne;

  assign branch = beq_and | bne_and;

  mux2_32 start_mux (pc_plus4, pc_branch, branch, pc_upgr);  

  mux2_32 jump_mux (pc_upgr, PC_jump, j, pc_for);

  mux2_32 jr_mux (pc_for, register_rd1, jr, pc_new);                      
              
endmodule