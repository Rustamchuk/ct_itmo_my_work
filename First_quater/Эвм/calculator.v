module my_not(a, out);
  input a;
  output out;
  supply1 sup;
  supply0 gnd;

  pmos p1 (out, sup, a);
  nmos n1 (out, gnd, a);
endmodule

module my_nor(a, b, out);
  input a;
  input b;
  output out;
  supply1 sup;
  supply0 gnd;
  wire w;
  
  pmos p1 (w, sup, a);
  pmos p2 (out, w, b);
  nmos n1 (out, gnd, a);
  nmos n2 (out, gnd, b);
endmodule

module my_or(a, b, out);
  input a;
  input b;
  output out;
  wire w;

  my_nor nor1 (a, b, w);
  my_not not1 (w, out);
endmodule

module my_nand(a, b, out);
  input a;
  input b;
  output out;
  supply1 sup;
  supply0 gnd;
  wire w;

  nmos n1 (w, gnd, b);
  nmos n2 (out, w, a);
  pmos p1 (out, sup, b);
  pmos p2 (out, sup, a);
endmodule

module my_and(a, b, out);
  input a;
  input b;
  output out;
  wire w;

  my_nand nand1 (a, b, w);
  my_not not1 (w, out);
endmodule

module my_xor(a, b, out);
  input a;
  input b;
  output out;
  wire w1, w2, w3, w4;

  my_not not1 (a, w1);
  my_and and1 (w1, b, w2);
  my_not not2 (b, w3);
  my_and and2 (a, w3, w4);
  my_or or1 (w2, w4, out);
endmodule

module my_imp(a, b, out);
  input a;
  input b;
  output out;
  wire w1;

  my_not not1 (a, w1);
  my_or or1 (w1, b, out);
endmodule

module my_imp_left(a, b, out);
  input a;
  input b;
  output out;
  wire w1;

  my_not not1 (b, w1);
  my_or or1 (a, w1, out);
endmodule

module invertor(a, out);
  input [3:0] a;
  reg [3:0] b = 4'b0001;
  output [3:0] out;

  wire [3:0] w1;
  
  my_not f (a[0], w1[0]);
  my_not ff (a[1], w1[1]);
  my_not fff (a[2], w1[2]);
  my_not ffff (a[3], w1[3]);
  summator l (w1, b, out);

endmodule

module subtract(a, b, out);
  input [3:0] a, b;
  output [3:0] out;
  wire [3:0] w1;
  
  invertor j (b, w1);
  summator f (a, w1, out);

endmodule

module add(a, b, out);
  input [3:0] a, b;
  output [3:0] out;
  
  summator l (a, b, out);

endmodule

module summator_between(a, b, c, out1, out2);
  input a, b, c;
  output out1, out2;
  wire w1, w2, w3, w4, w5, w6;

  my_xor one (a, b, w1);
  my_xor third (w1, c, out1);

  my_and second (a, b, w2);
  my_and four (w1, c, w3);
  my_or five (w2, w3, out2);

endmodule

module summator(a, b, out);
  input [3:0] a, b;
  reg c = 0;
  output [3:0] out;
  wire w1, w2, w3, w4;
  
  summator_between one (a[0], b[0], c, out[0], w1);
  summator_between two (a[1], b[1], w1, out[1], w2);
  summator_between thr (a[2], b[2], w2, out[2], w3);
  summator_between fou (a[3], b[3], w3, out[3], w4);

endmodule

module conunction(a, b, out);
  input [3:0] a, b;
  output [3:0] out;
  
  my_and one (a[0], b[0], out[0]);
  my_and two (a[1], b[1], out[1]);
  my_and thr (a[2], b[2], out[2]);
  my_and fou (a[3], b[3], out[3]);

endmodule  

module disunction(a, b, out);
  input [3:0] a, b;
  output [3:0] out;
  
  my_or one (a[0], b[0], out[0]);
  my_or two (a[1], b[1], out[1]);
  my_or thr (a[2], b[2], out[2]);
  my_or fou (a[3], b[3], out[3]);

endmodule 

module conunction_not(a, b, out);
  input [3:0] a, b;
  output [3:0] out;
  wire w1, w2, w3, w4;
  
  my_not not_one (b[0], w1);
  my_not not_two (b[1], w2);
  my_not not_thr (b[2], w3);
  my_not not_fou (b[3], w4);
  
  my_and and_one (a[0], w1, out[0]);
  my_and and_two (a[1], w2, out[1]);
  my_and and_thr (a[2], w3, out[2]);
  my_and and_fou (a[3], w4, out[3]);

endmodule 

module disunction_not(a, b, out);
  input [3:0] a, b;
  output [3:0] out;
  wire w1, w2, w3, w4;
  
  my_not not_one (b[0], w1);
  my_not not_two (b[1], w2);
  my_not not_thr (b[2], w3);
  my_not fnot_foufff (b[3], w4);
  
  my_or or_one (a[0], w1, out[0]);
  my_or or_two (a[1], w2, out[1]);
  my_or or_thr (a[2], w3, out[2]);
  my_or or_fou (a[3], w4, out[3]);

endmodule 

module main_SLT_check(l_sim, l_less, r_sim, r_less, sim, less);
  input l_sim, l_less, r_sim, r_less;
  output sim, less;

  wire w1;

  my_and and_one (l_sim, r_sim, sim);
  my_and and_two (l_less, r_sim, w1);
  my_or or_one (w1, r_less, less);

endmodule

module first_SLT_check(l, r, sim, less);
  input l, r;
  output sim, less;

  wire w1, w2;

  my_xor xor_one (l, r, w1);
  my_not not_one (w1, sim);

  my_not not_two (l, w2);
  my_and and_one (r, w2, less);

endmodule

module high_SLT_check(l, r, sim, upper);
  input l, r;
  output sim, upper;

  wire w1, w2;

  my_xor xor_one (l, r, w1);
  my_not not_one (w1, sim);

  my_not not_two (r, w2);
  my_and and_one (l, w2, upper);

endmodule

module slt(a, b, out);
  input [3:0] a, b;
  output [3:0] out;
  wire w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13, w14;
  wire zero = 0;
  
  first_SLT_check one (a[0], b[0], w1, w2);
  first_SLT_check two (a[1], b[1], w3, w4);
  main_SLT_check three (w1, w2, w3, w4, w5, w6);

  first_SLT_check four (a[2], b[2], w7, w8);
  high_SLT_check five (a[3], b[3], w9, w10);
  main_SLT_check six (w7, w8, w9, w10, w11, w12);

  main_SLT_check seven (w5, w6, w11, w12, w13, w14);

  my_and and_one (a[3], zero, out[3]);
  my_and and_two (a[2], zero, out[2]);
  my_and and_three (a[1], zero, out[1]);
  my_or or_four (zero, w14, out[0]);

endmodule

module not_used(a, b, out);
  input [3:0] a, b;
  output [3:0] out;

endmodule

module multiplecsor(control, con, diz, ad, nott, con_not, diz_not, sub, slt, out);
  input [2:0] control;
  input con, diz, ad, nott, con_not, diz_not, sub, slt;
  output out;

  wire con_res, diz_res, ad_res, nott_res, con_not_res, diz_not_res, sub_res, slt_res;

  wire control_not_one, control_not_two, control_not_three;

  wire w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13, w14, w15, w16, w17, w18, w19, w20;

  my_not one (control[2], control_not_one);
  my_not two (control[1], control_not_two);
  my_not three (control[0], control_not_three);

  my_and conunct_one (control_not_one, control_not_two, w1);
  my_and conunct_two (w1, control_not_three, w2);
  my_and conunct_three (w2, con, con_res);

  my_and dizunct_two (w1, control[0], w3);
  my_and dizunct_three (w3, diz, diz_res);

  my_and add_one (control_not_one, control[1], w4);
  my_and add_two (w4, control_not_three, w5);
  my_and add_three (w5, ad, ad_res);

  my_and nott_one (control_not_one, control[1], w6);
  my_and nott_two (w6, control[0], w7);
  my_and nott_three (w7, nott, nott_res);

  my_and conunct_not_one (control[2], control_not_two, w8);
  my_and conunct_not_two (w8, control_not_three, w9);
  my_and conunct_not_three (w9, con_not, con_not_res);

  my_and dizunct_not_two (w8, control[0], w10);
  my_and dizunct_not_three (w10, diz_not, diz_not_res);

  my_and substr_one (control[2], control[1], w11);
  my_and substr_two (w11, control_not_three, w12);
  my_and substr_three (w12, sub, sub_res);

  my_and slt_one (control[2], control[1], w13);
  my_and slt_two (w13, control[0], w14);
  my_and slt_three (w14, slt, slt_res);

  my_or con_diz_one (con_res, diz_res, w15);
  my_or con_diz_two (w15, ad_res, w16);
  my_or con_diz_three (w16, nott_res, w17);
  my_or con_diz_four (w17, con_not_res, w18);
  my_or con_diz_five (w18, diz_not_res, w19);
  my_or con_diz_six (w19, sub_res, w20);
  my_or con_diz_seven (w20, slt_res, out);

endmodule

module alu(a, b, control, res);
  input [3:0] a, b; // Операнды
  input [2:0] control; // Управляющие сигналы для выбора операции

  output [3:0] res; // Результат
  
  wire [3:0] con_res, diz_res, add_res, nott_res, con_not_res, diz_not_res, sub_res, slt_res;
  
  conunction f (a, b, con_res);
  disunction g (a, b, diz_res);
  add j (a, b, add_res);
  not_used ff (a, b, nott_res);
  conunction_not gg (a, b, con_not_res);
  disunction_not jj (a, b, diz_not_res);
  subtract fg (a, b, sub_res);
  slt gf (a, b, slt_res);

  multiplecsor mult_one (control, con_res[0], diz_res[0], add_res[0], nott_res[0], con_not_res[0], diz_not_res[0], sub_res[0], slt_res[0], res[0]);
  multiplecsor mult_two (control, con_res[1], diz_res[1], add_res[1], nott_res[1], con_not_res[1], diz_not_res[1], sub_res[1], slt_res[1], res[1]);
  multiplecsor mult_thr (control, con_res[2], diz_res[2], add_res[2], nott_res[2], con_not_res[2], diz_not_res[2], sub_res[2], slt_res[2], res[2]);
  multiplecsor mult_fou (control, con_res[3], diz_res[3], add_res[3], nott_res[3], con_not_res[3], diz_not_res[3], sub_res[3], slt_res[3], res[3]);

endmodule

module multiplecsor_by_two(control, a, b, c, d, out);
input [1:0] control;
input a, b, c, d;
output out;

wire not_one, not_two;

my_not not_one_one (control[1], not_one);
my_not not_two_two (control[0], not_two);

my_and and_one (not_one, not_two, w1);
my_and and_two (w1, a, w2);

my_and and_thr (not_one, control[0], w3);
my_and and_fou (w3, b, w4);

my_and and_fiv (control[1], not_two, w5);
my_and and_six (w5, c, w6);

my_and and_sev (control[1], control[0], w7);
my_and and_eig (w7, d, w8);

my_or or_one (w2, w4, w9);
my_or or_two (w6, w8, w10);
my_or or_thr (w9, w10, out);

endmodule

module decoder(a, b);
  input [1:0] a;
  output [3:0] b;
  wire not_one, not_two;

  my_not not_one_one (a[1], not_one);
  my_not not_two_two (a[0], not_two);

  my_and and_one (not_one, not_two, b[0]);
  my_and and_two (not_one, a[0], b[1]);
  my_and and_thr (a[1], not_two, b[2]);
  my_and and_fou (a[1], a[0], b[3]);
endmodule

module d_latch(clk, d, we, q);
  input clk; // Сигнал синхронизации
  input d; // Бит для записи в ячейку
  input we; // Необходимо ли перезаписать содержимое ячейки

  output reg q; // Сама ячейка
  // Изначально в ячейке хранится 0
  initial begin
    q <= 0;
  end
  // Значение изменяется на переданное на спаде сигнала синхронизации
  always @ (negedge clk) begin
    if (we) begin
      q <= d;
    end
  end
endmodule

module register_file(clk, rd_addr, we_addr, we_data, rd_data);
  input clk; // Сигнал синхронизации
  input [1:0] rd_addr, we_addr; // Номера регистров для чтения и записи
  input [3:0] we_data; // Данные для записи в регистровый файл

  output [3:0] rd_data; // Данные, полученные в результате чтения из регистрового файла

  wire [3:0] w1, w2, w3, w4, dec_one;

  decoder dec (we_addr, dec_one);
  register reg_one (clk, dec_one[0], we_data, w1);
  register reg_two (clk, dec_one[1], we_data, w2);
  register reg_thr (clk, dec_one[2], we_data, w3);
  register reg_fou (clk, dec_one[3], we_data, w4);
  multiplecsor_by_two mux_one (rd_addr, w1[0], w2[0], w3[0], w4[0], rd_data[0]);
  multiplecsor_by_two mux_two (rd_addr, w1[1], w2[1], w3[1], w4[1], rd_data[1]);
  multiplecsor_by_two mux_thr (rd_addr, w1[2], w2[2], w3[2], w4[2], rd_data[2]);
  multiplecsor_by_two mux_fou (rd_addr, w1[3], w2[3], w3[3], w4[3], rd_data[3]);
  
endmodule

module register(clk, we, we_data, rd_data);
  input clk; // Сигнал синхронизации
  input [3:0] we_data; // Данные для записи в регистровый файл
  input we; // Необходимо ли перезаписать содержимое ячейки

  output [3:0] rd_data; // Данные, полученные в результате чтения из регистрового файла

  d_latch d_one (clk, we_data[0], we, rd_data[0]);
  d_latch d_two (clk, we_data[1], we, rd_data[1]);
  d_latch d_thr (clk, we_data[2], we, rd_data[2]);
  d_latch d_fou (clk, we_data[3], we, rd_data[3]);
  
endmodule

module calculator(clk, rd_addr, immediate, we_addr, control, rd_data);
  input clk; // Сигнал синхронизации
  input [1:0] rd_addr; // Номер регистра, из которого берется значение первого операнда
  input [3:0] immediate; // Целочисленная константа, выступающая вторым операндом
  input [1:0] we_addr; // Номер регистра, куда производится запись результата операции
  input [2:0] control; // Управляющие сигналы для выбора операции

  output [3:0] rd_data; // Данные из регистра c номером 'rd_addr', подающиеся на выход

  wire [3:0] we_data;

  register_file regist (clk, rd_addr, we_addr, we_data, rd_data);

  alu alu_one (rd_data, immediate, control, we_data);

endmodule