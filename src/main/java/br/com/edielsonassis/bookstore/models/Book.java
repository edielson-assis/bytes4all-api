package br.com.edielsonassis.bookstore.models;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "bookId")
@Setter
@Getter
@Entity
@Table(name = "books")
public class Book implements Serializable {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
	private Long bookId;
	
	@Column(nullable = false, length = 150)
	private String author;

	@Column(name = "launch_date", nullable = false)
	private LocalDate launchDate;
	
	@Column(nullable = false, length = 255)
	private String title;

    @Column(nullable = false, length = 500)
    private String description;

	@Column(name = "download_url", nullable = false)
	private String downloadUrl;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}