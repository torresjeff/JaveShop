package com.javeshop.javeshop.services.entities;

/**
 * Data Transfer Object (DTO) que permite intercambiar informacion con el servidor.
 * En este DTO se lleva la informacion de un comentario de un producto en especifico.
 */
public class ProductComment
{
    private int productId;
    private int commentId;
    private int commenterId;
    private String question;
    private int replierId;
    private String reply;

    public ProductComment(int productId, int commenterId, String question)
    {
        this.productId = productId;
        this.commenterId = commenterId;
        this.question = question;
        //replierId = -1;
    }

    public ProductComment(int productId, int commentId, int commenterId, String question, int replierId, String reply)
    {
        this.productId = productId;
        this.commentId = commentId;
        this.commenterId = commenterId;
        this.question = question;
        this.replierId = replierId;
        this.reply = reply;
    }

    public int getProductId()
    {
        return productId;
    }

    public int getCommentId()
    {
        return commentId;
    }

    public int getCommenterId()
    {
        return commenterId;
    }

    public String getQuestion()
    {
        return question;
    }

    public int getReplierId()
    {
        return replierId;
    }

    public String getReply()
    {
        return reply;
    }
}
