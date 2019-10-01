/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.teste.presenter;

import br.com.teste.dao.ItemtableDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.com.teste.model.Itemtable;

/**
 *
 * @author wesllen
 */
public class ItemtableSlv extends HttpServlet{
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AlunoSvl</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AlunoSvl at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    
    
    
    public static List<Itemtable> obterAlunos() {
        List<Itemtable> lItemtable;
        lItemtable = ItemtableDAO.getInstance().obterTodosOsItems();
        
        return lItemtable;
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
        String id = request.getParameter("id");
        String op = request.getParameter("op");
        Itemtable pItemtable = new Itemtable();
        pItemtable.setId(Integer.parseInt(id));
        pItemtable = ItemtableDAO.getInstance().obterItemtablePorChave(pItemtable);
        
        //Se op=1 então alterar
        if (op.equals("1")){
  
            request.setAttribute("Itemtable", pItemtable);
            request.getRequestDispatcher("Itemtable_form.jsp").forward(request, response);
            
         //Se op=2 então excluir
         }else if (op.equals("2")){
            
             boolean result = AlunoDAO.getInstance().removerAluno(pAluno);
            
             //Se foi excluido com sucesso result = true
             if (result){
                 request.setAttribute("msg", "Excluido com sucesso!");
                 request.getRequestDispatcher("aluno_lista.jsp").forward(request, response);  
             }else{
             //houve erro na exclusao entao result = false
                 request.setAttribute("msg_erro", "Erro na Exclusao!");
                 request.getRequestDispatcher("aluno_lista.jsp").forward(request, response);  
             
             }
             
         }
        
        
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        String param_msg;
        Aluno pAluno = new Aluno();
        
       
        //Trata erro no tipo de dados do semestre
        try{
            pAluno.setSemestre(Integer.parseInt(request.getParameter("semestre")));
        }catch(Exception ex){
            ex.printStackTrace();
            
            request.setAttribute("msg_error", "O campo semestre deve possuir um valor numérico");
            request.getRequestDispatcher("aluno_form.jsp").forward(request, response); 
        
        }
        
        //verifica se é uma atualização ou nova inserção
        //atualizacao é enviado parâmetro com o id do aluno
        if(request.getParameter("id_aluno")!= null && !request.getParameter("id_aluno").equals("")){
            pAluno.setIdAluno(Integer.parseInt(request.getParameter("id_aluno")));
            pAluno = AlunoDAO.getInstance().obterAlunoPorChave(pAluno);
            request.setAttribute("msg", pAluno.getPessoa().getNome() + " " + pAluno.getPessoa().getSobrenome() + " atualizado com sucesso!");
            param_msg = " atualizado com sucesso!";
     
        }else{
             param_msg = " inserido com sucesso!";
        }
        
        if (pAluno.getPessoa() == null) pAluno.setPessoa(new Pessoa());
        
        pAluno.getPessoa().setNome(request.getParameter("nome"));
        pAluno.getPessoa().setSobrenome(request.getParameter("sobrenome"));
        pAluno.getPessoa().setEmail(request.getParameter("email"));
 
        //tenta carregar o genero
        try {
            Genero lGenero = new Genero();
            lGenero.setIdGenero(Integer.parseInt(request.getParameter("genero")));
            pAluno.getPessoa().setGenero(GeneroDAO.getInstance().obterGeneroPorChave(lGenero));
            
        }catch(Exception e){
            e.printStackTrace();
        }

        pAluno.getPessoa().setMatricula(request.getParameter("matricula"));
        pAluno.getPessoa().setUsuario(request.getParameter("login"));
        
        //Altera a senha apenas se o campo estiver preenchido
        if (request.getParameter("pwd")!= null && !request.getParameter("pwd").equals(""))
            pAluno.getPessoa().setSenha(request.getParameter("pwd"));
        
        //Controle de exceção para erro na conversão das datas
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            pAluno.getPessoa().setDataIngresso(sdf.parse(request.getParameter("data_ingresso")));
            pAluno.getPessoa().setDataNascimento(sdf.parse(request.getParameter("data_nascimento")));
        } catch (ParseException ex) {
            Logger.getLogger(AlunoSvl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        pAluno = AlunoDAO.getInstance().salvaAluno(pAluno);
        
        request.setAttribute("msg", pAluno.getPessoa().getNome() + " " + pAluno.getPessoa().getSobrenome() + param_msg);
     
        request.getRequestDispatcher("aluno_form.jsp").forward(request, response);  
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
