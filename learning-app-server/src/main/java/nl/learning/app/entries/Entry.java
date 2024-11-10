package nl.learning.app.entries;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class Entry {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Basic(optional=false)
  @Column(updatable=false)
  private Long id;
  private String subject;
  private String date;
  private String time;
  private String notes;
  private String todo;
}
