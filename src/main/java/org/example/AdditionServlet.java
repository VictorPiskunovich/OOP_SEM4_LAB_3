package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(urlPatterns = {"/addition"})
public class AdditionServlet extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        JSONObject movieJson = new JSONObject();
        movieJson.put("name", req.getParameter("name"));
        movieJson.put("director", req.getParameter("director"));
        movieJson.put("year", req.getParameter("year"));
        movieJson.put("fees", req.getParameter("fees"));
        movieJson.put("genre", req.getParameter("genre"));
        movieJson.put("budget", req.getParameter("budget"));

        // путь к файлу JSON
        String jsonPath = getJsonPath();

        // содержимое JSON
        String jsonContent = new String(Files.readAllBytes(Path.of(jsonPath)));

        // создаем JSONArray из строки JSON
        JSONArray jsonArray = new JSONArray(jsonContent);

        jsonArray.put(movieJson);

        // обновить локально
        try (FileWriter writer = new FileWriter(jsonPath))
        {
            writer.write(jsonArray.toString(4));
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        // обновить на сервере
        try (FileWriter writer = new FileWriter("movies.json"))
        {
            writer.write(jsonArray.toString(4));
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        // обновить страницу для обновления таблицы
        updatePage(req, resp);
    }

    private String getJsonPath()
    {
        String parentPath;
        try
        {
            parentPath = new File(AdditionServlet.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getParent();
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }

        return parentPath + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "movies.json";
    }

    private void updatePage(ServletRequest request, ServletResponse response) throws ServletException, IOException
    {
        RequestDispatcher view = request.getRequestDispatcher("index.html");
        view.forward(request, response);
    }
}