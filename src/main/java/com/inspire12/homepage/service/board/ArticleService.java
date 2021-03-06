package com.inspire12.homepage.service.board;


import com.fasterxml.jackson.databind.node.ObjectNode;

import com.inspire12.homepage.message.ArticleMsg;
import com.inspire12.homepage.message.CommentMsg;
import com.inspire12.homepage.model.entity.Article;
import com.inspire12.homepage.model.entity.Comment;
import com.inspire12.homepage.model.entity.User;
import com.inspire12.homepage.repository.ArticleRepository;
import com.inspire12.homepage.repository.CommentRepository;
import com.inspire12.homepage.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    public ArticleMsg showArticleMsgById(int id) {

        articleRepository.increaseHit(id);
        return getArticleMsgById(id);
    }

    public ArticleMsg getArticleMsgById(int id) {
        Article article = articleRepository.findById(id).get();
        return ArticleMsg.create(article);
    }

    public List<ArticleMsg> showArticleMsgsWithCount(int type, int pageNum, int articleCount) {
        int start = (pageNum - 1) * articleCount;
        List<Article> articles;
        if (type == 0) {
            articles = articleRepository.showArticlesWithArticleCount(start, articleCount);
        } else {
            articles = articleRepository.showArticlesWithArticleByTypeCount(type, start, articleCount);
        }
        return convertArticles(articles);
    }
    public Article updateArticle(ObjectNode objectNode) {
        // 데이터 검증
        int id = objectNode.get("id").asInt();

        Article article = articleRepository.findById(id).get();
        article.setSubject(objectNode.get("title").asText());
        article.setContent(objectNode.get("content").asText());
        article.setBoardId(objectNode.get("type").asInt());
        articleRepository.save(article);

        return article;
    }

    public List<ArticleMsg> showArticleMsgs() {
        List<Article> articles = articleRepository.selectArticles(30);
        return convertArticles(articles);
    }

    public boolean saveArticle(Article article) {
        // 데이터 검증
        articleRepository.save(article);
        article.setGrpno(article.getId());
        articleRepository.save(article);
        return true;
    }


    @Transactional
    public boolean saveArticleReply(int parentId, Article childArticle) {
        Article parentArticle = articleRepository.findById(parentId).get();
        articleRepository.updateReplyOrder(parentArticle.getGrpno(), parentArticle.getGrpord());

        childArticle.setGrpno(parentArticle.getGrpno());
        childArticle.setGrpord(parentArticle.getGrpord() + 1);
        childArticle.setDepth(parentArticle.getDepth() + 1);

        articleRepository.save(childArticle);
        return true;
    }

    public boolean modifyArticle(Article article) {
        articleRepository.save(article);
        return true;
    }

    public boolean deleteArticle(int articleId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (username.equals(articleRepository.getOne(articleId).getUsername())) {
            articleRepository.updateIsDeletedArticle(articleId);
            return true;
        }
        return false;
    }

    private List<ArticleMsg> convertArticles(List<Article> articles) {
        List<ArticleMsg> articleMsgs = new ArrayList<>();
        for (Article article : articles) {
            try {
                articleMsgs.add(ArticleMsg.create(article));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return articleMsgs;
    }

    private List<ArticleMsg> convertArticlesToArticleMsgs(List<Article> articles) {
        List<ArticleMsg> articleMsgs = new ArrayList<>();
        for (Article article : articles) {
            try {
                User author = userRepository.findById(article.getUsername()).get();
                List<Comment> comments = commentRepository.selectCommentByArticleOrder(article.getId());
                articleMsgs.add(ArticleMsg.createWithComments(article, author, convertToMsg(comments)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return articleMsgs;
    }

    private List<CommentMsg> convertToMsg(List<Comment> comments) {
        List<CommentMsg> commentMsgs = new ArrayList<>();
        for (Comment comment : comments) {
            try {
                commentMsgs.add(CommentMsg.createCommentMsg(comment, userRepository.getOne(comment.getUsername())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return commentMsgs;
    }
}
