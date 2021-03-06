package com.fajar.arabicclub.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fajar.arabicclub.constants.AnswerCode;
import com.fajar.arabicclub.dto.model.QuizChoiceModel;
import com.fajar.arabicclub.dto.model.QuizQuestionModel;
import com.fajar.arabicclub.entity.setting.SingleImageModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 
@Entity
@Table(name = "quiz_question")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class QuizQuestion extends BaseEntity<QuizQuestionModel> implements SingleImageModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 3494963248002164943L;
	@Column(nullable = false, name = "answer_code")
	@Enumerated(EnumType.STRING)
	private AnswerCode answerCode;

	@Column(nullable = false)
	private String statement;
	@Column
	private Integer number;

	@JoinColumn(name = "quiz_id", nullable = false)
	@ManyToOne
	@JsonIgnore
	private Quiz quiz;

	@Column
	private String image;
	@Column
	private long duration;

	@Transient
	private List<QuizChoice> choices;
	@Transient
	private AnswerCode correctChoice;

	@JsonIgnore
	public Long getQuizId() {
		if (quiz == null) {
			return null;
		}
		return quiz.getId();
	}
	
	@Override
	public QuizQuestionModel toModel() {
	  
		QuizQuestionModel model = copy(new QuizQuestionModel(), "choices");
		List<QuizChoiceModel> choicesModel = new ArrayList<QuizChoiceModel>();
		
		if (null != choices) {
			choices.forEach(q -> {
				choicesModel.add(q.toModel());
			});
		}
		model.setChoices(choicesModel );
		return model;
	}

	public void preventStackOverFlowError() {
		if (null == choices) return;
		choices.forEach(c->c.setQuestion(null));
		
	}

}
