# PROJECT_STATE — AuraShow

Last reviewed: **2026-10-12 (America/New_York)**. Continuity schema: **1**.

This is a maintained project handoff, not an assertion that every historical chat or runtime behavior has been verified. Recheck Git and source at session start. Architecture and feature notes below describe the audited local project unless explicitly labeled remote default.

## End goal

An Android ambient slideshow with user-selected images, clock/weather overlays, and weather effects; charging-triggered behavior is a product goal, not established as complete.

## Architecture

Kotlin Android app; slideshow and weather view models, preferences, Compose-style UI components and weather-effect renderers.

## Decisions and workflow

- Keep weather credentials outside committed source.
- Treat README architectural pillars as intended scope until confirmed by implementation and runtime checks.
- Preserve existing acceptance gates and uncommitted work. The 2026-10-12 authorization covers continuity documentation branches only, not merging features, deploying, or pushing unrelated code.

## Current status

- Local working branch at audit: `main`; code snapshot `741c7392a27ca8e548442fec1e43dd4473699cd0`.
- Pre-existing working-tree changes: **0** entries. These were not staged, committed or discarded by continuity setup.
- Live GitHub default: `main` at `741c7392a27ca8e548442fec1e43dd4473699cd0`.
- Documentation is prepared on `docs/project-continuity-2026-10-12` from that default. Feature-branch implementation described below is not thereby merged.
- Repository: https://github.com/ZuruiKyoku/AuraShow (public).

## Completed work

These are source/history findings; historical test reports are labeled and are not fresh runtime verification.

- Source includes slideshow, settings, image-fit/transition controls, weather service and individual weather effects.
- README describes the project as work in progress; Git history records duration-step and scrollbar fixes.

## Known issues and gaps

- No project-specific ChatGPT transcript was returned in the bounded chat inventory.
- Current transition choices, charging activation, permissions and live weather behavior have not been runtime-tested in this audit.
- No directly associated local coding transcript was found in the reviewed inventory. Project goals come from source/README unless explicitly marked inferred.
- ChatGPT discovery returned a bounded recent list, not every project conversation. Some retrieved assistant messages contain only content-reference placeholders; attachments and unavailable responses were not reconstructed.

## Roadmap

- Run slideshow, transitions and weather on a device; confirm charging-trigger requirements.
- Recover the AuraShow project chats to reconcile the roadmap.
- Reconcile the missing historical context before treating this document as a complete replacement for old chats.

## Next steps

1. Read this file and applicable `AGENTS.md`; inspect the current branch, working tree and remote refs.
2. Run slideshow, transitions and weather on a device; confirm charging-trigger requirements.
3. Perform the project's required checks for any code change, obtain existing acceptance where required, and update this handoff with actual results.

## Verification and evidence

- This session verified repository identity, local Git metadata/tracked paths, live remote refs where configured, and inspected documentation/source evidence. It changed documentation only.
- No application build, unit-test suite, device test, deployment check or live-service test is implied by this audit. Prior successful checks do not validate later uncommitted edits.
- Evidence labels: **source present** is static evidence; **historical report** is a past session claim; **verified now** requires an explicitly recorded current check; **planned/unknown** is not implemented.

- `app/src/main/java/com/slygames/aurashow/ui/components/Slideshow.kt` — present on remote default; same content locally.
- `app/src/main/java/com/slygames/aurashow/data/WeatherService.kt` — present on remote default; same content locally.

### History sources and coverage

No directly associated local coding transcript was found in the reviewed inventory. Project goals come from source/README unless explicitly marked inferred.

- No matching local transcript identified.

## Session handoff

- Last task: establish PROJECT_STATE.md and additive agent instructions from available project evidence.
- Documentation scope: preserve all pre-existing code, assets, branches, indexes and acceptance gates. A documentation branch does not back up uncommitted application work.
- Publication state: consult the actual Git log/remote branch; do not assume a prepared branch was pushed or merged. The continuity report records the verified publication result.
- Next owner: follow Next steps above, fill historical gaps, and replace stale status with dated evidence after meaningful work.
- Archive readiness: incomplete history and any local-only implementation still need reconciliation; do not infer that archiving all project chats is lossless.
